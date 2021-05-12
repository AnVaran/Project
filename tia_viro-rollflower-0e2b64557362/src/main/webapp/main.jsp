<%@ page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.TreeMap"%>
<%@ page import="java.lang.StringBuffer"%>
<%@ page import="application.model.Flower"%>
<%@ page import="application.model.Decoration"%>
<%@ page import="application.model.Wrapper"%>
<%@ page import="application.model.User"%>
<%@ page import="java.util.Optional"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel='stylesheet' type="text/css" href="styles/mainPageStyle.css">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
<script src="scripts/mainPageScript.js"></script>
<title>RollFlower</title>
</head>
<body>
	<div class="container">
		<nav class="navbar navbar-expand-lg navbar-light bg-light">
			<a class="navbar-brand" href="/rollflower/">RollFlower</a>
			<div class="collapse navbar-collapse" id="navbarSupportedContent">
				<ul class="navbar-nav">
					<li class="nav-item active"><a class="nav-link homeMenuButton">Home<span
							class="sr-only">(current)</span>
					</a></li>
					<li class="nav-item"><a class="nav-link orderMenuButton">Order
							<span class="badge badge-success order-badge" hidden="true">0</span>
					</a></li>

				</ul>
				<%
					Optional<User> user = Optional.ofNullable((User) request.getAttribute("authorized"));
					if (user.isPresent()) {
				%>
				<ul class="navbar-nav ml-auto dropdown">
					<li class="nav-item dropdown"><a
						class="nav-link dropdown-toggle" href="#" id="navbarDropdown"
						role="button" data-toggle="dropdown" aria-haspopup="true"
						aria-expanded="false"><%=user.isPresent() ? user.get().getEmail() : "-"%></a>
						<div class="dropdown-menu" aria-labelledby="navbarDropdown">
							<a class="dropdown-item" href="/rollflower/cabinet">Profile</a>
							<div class="dropdown-divider"></div>
							<a class="dropdown-item" href="/rollflower/logout">Log out</a>
						</div></li>
				</ul>
				<%
					} else {
				%>
				<ul class="navbar-nav ml-auto">
					<li class="nav-item"><a class="nav-link"
						href="/rollflower/security">Log in / Register</a></li>
				</ul>
				<%
					}
				%>
			</div>
		</nav>
		<div class="jumbotron">
			<h3>Create your own flower bouquets for free!</h3>
		</div>
		<div class="row text-center">
			<div class="col">
				<div class="cliente">
					<p>Flower</p>
					<select class="flowerSelect" required size="3" style="width: 80%">
						<%
							Map<Long, Flower> flowerMap = (Map<Long, Flower>) request.getAttribute("flowers");
							for (Map.Entry<Long, Flower> entry : flowerMap.entrySet()) {
								StringBuffer sb = new StringBuffer();
								sb.append(entry.getValue().getColor().toString().substring(0, 1));
								sb.append(entry.getValue().getColor().toString()
										.substring(1, entry.getValue().getColor().toString().length()).toLowerCase());
								sb.append(" ");
								sb.append(entry.getValue().getName().toLowerCase());
								sb.append(", ");
								sb.append(entry.getValue().getLength().toString().toLowerCase());
								String fullName = sb.toString();
						%>
						<option value="<%=entry.getKey()%>"
							data-flower-price="<%=entry.getValue().getPrice()%>"><%=fullName%></option>
						<%
							}
						%>
					</select>
					<div class="flowerCompose" hidden="true">
						<label style='margin-top: 20px'>Select quantity:</label> <span
							class="quantityInputWrapper" style="width: 80%"><input
							class="flowerQuantity" type="number" min="1" max="100" value="1"
							onkeydown="return false"></span> <label class="flowerPriceLabel"
							data-current-price=""></label>
						<button class="addFlowerTOBouquetButton" data-pressed="false">Add
							To Bouquet</button>
					</div>

				</div>
			</div>
			<div class="col">
				<div class="cliente">
					<p>Decorations</p>
					<select class="decorationSelect" size="3" style="width: 80%">
						<option value="0" selected="selected">None</option>
						<%
							Map<Long, Decoration> decorationMap = (Map<Long, Decoration>) request.getAttribute("decorations");
							for (Map.Entry<Long, Decoration> entry : decorationMap.entrySet()) {
						%>
						<option value="<%=entry.getKey()%>"
							data-decoration-price="<%=entry.getValue().getPrice()%>"><%=entry.getValue().getName()%></option>
						<%
							}
						%>
					</select> <label class="decorationPriceLabel" hidden="true"></label>
				</div>
			</div>
			<div class="col">
				<div class="cliente">
					<p>Wrapper</p>
					<select class="wrapperSelect" size="3" style="width: 80%">
						<option value="0" selected="selected">None</option>
						<%
							Map<Long, Wrapper> wrapperMap = (Map<Long, Wrapper>) request.getAttribute("wrappers");
							for (Map.Entry<Long, Wrapper> entry : wrapperMap.entrySet()) {
						%>
						<option value="<%=entry.getKey()%>"
							data-wrapper-price="<%=entry.getValue().getPrice()%>">
							<%=entry.getValue().getName()%>
						</option>
						<%
							}
						%>
					</select> <label class="wrapperPriceLabel" hidden="true"></label>
				</div>
			</div>
		</div>
		<button type="button" class="btn btn-dark btn-block addBouquetButton"
			disabled>Add To Order</button>
	</div>
</body>
</html>
