<%@ page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.lang.StringBuffer"%>
<%@ page import="application.model.Flower"%>
<%@ page import="application.model.Decoration"%>
<%@ page import="application.model.Wrapper"%>
<%@ page import="application.model.FlowerBouquet"%>
<%@ page import="application.model.component.FlowerComponent"%>
<%@ page import="application.model.User"%>
<%@ page import="java.util.Optional"%>
<!DOCTYPE html>
<html>
<head>
<link rel='stylesheet' type="text/css" href="styles/mainPageStyle.css">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
<script src="scripts/orderPageScript.js"></script>
<title>Order</title>
</head>
<body>
	<%
		Optional<User> user = Optional.ofNullable((User) request.getAttribute("authorized"));
		if (request.getAttribute("orderSubmit") != null && request.getAttribute("orderSubmit").equals(true)) {
	%>
	<div class="modal fade" tabindex="-1" role="dialog"
		aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered" role="document">
			<div class="modal-content"></div>
		</div>
	</div>
	<%
		}
	%>
	<div class="container">
		<nav class="navbar navbar-expand-lg navbar-light bg-light">
			<a class="navbar-brand" href="/rollflower">RollFlower</a>
			<div class="collapse navbar-collapse" id="navbarSupportedContent">
				<ul class="navbar-nav">
					<li class="nav-item"><a class="nav-link" href="/rollflower">Home<span
							class="sr-only">(current)</span>
					</a></li>
					<li class="nav-item orderMenuButton active"><a
						class="nav-link" href="http://localhost:8080/rollflower/order">Order</a></li>
				</ul>
				<%
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
			<h3>Get your order information.</h3>
			<%
				if (request.getAttribute("orderSubmit") == null) {
			%>
			<form class="form-inline mr-auto search-order">
				<input class="form-control mr-sm-2" type="text" placeholder="Search"
					aria-label="Search">
				<button class="btn blue-gradient btn-rounded btn-sm my-0"
					type="submit">Search</button>
			</form>
			<%
				}
			%>
		</div>
		<%
			if (request.getAttribute("orderSubmit") != null && request.getAttribute("orderSubmit").equals(true)) {
		%>
		<jsp:include page="component/orderSubmit.jsp" />
		<%
			} else if (request.getAttribute("orderSubmit") == null) {
		%>
		<div class="result-table-container"></div>
		<%
			}
		%>
	</div>
</body>
</html>