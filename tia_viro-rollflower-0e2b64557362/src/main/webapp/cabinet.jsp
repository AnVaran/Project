<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<%@ page import="application.model.User"%>
<%@ page import="java.util.Optional"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
<script src="/rollflower/scripts/cabinetPageScript.js"></script>
<title>Cabinet</title>
</head>
<body>
	<%
		Optional<User> user = Optional.ofNullable((User) request.getAttribute("user"));
		if (request.getAttribute("message") != null) {
	%>
	<div class="alert alert-info"
		style="position: fixed; z-index: 100; width: 100%">
		<a class="close" data-dismiss="alert" aria-label="close">&times;</a><strong><%=request.getAttribute("message")%></strong>
	</div>
	<%
		}
	%>
	<div class="modal fade" tabindex="-1" role="dialog"
		aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered" role="document">
			<div class="modal-content">
				<div class="container" style="text-align: center;">
					<p>Confirm your intentions.
					<div class="input-group input-group-sm mb-3">
						<div class="input-group-prepend">
							<span class="input-group-text" id="inputGroup-sizing-sm">Password:</span>
						</div>
						<input type="password" class="form-control delete-password"
							aria-label="Small" aria-describedby="inputGroup-sizing-sm">
					</div>
				</div>
				<div class="container"
					style="text-align: center; margin-bottom: 20px">
					<button type="button" class="btn btn-danger confirm-delete"
						style="margin-right: 50px">Yes</button>
					<button type="button" class="btn btn-default cancel-delete"
						style="margin-left: 50px">No</button>
				</div>
			</div>
		</div>
	</div>
	<div class="container">
		<nav class="navbar navbar-expand-lg navbar-light bg-light"> <a
			class="navbar-brand" href="/rollflower/">RollFlower</a>
		<div class="collapse navbar-collapse" id="navbarSupportedContent">
			<ul class="navbar-nav">
				<li class="nav-item"><a class="nav-link homeMenuButton"
					href="/rollflower/">Home </a></li>
				<li class="nav-item"><a class="nav-link orderMenuButton"
					href="/rollflower/order">Order </a></li>
			</ul>
			<ul class="navbar-nav ml-auto dropdown">
				<li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" href="#" id="navbarDropdown"
					role="button" data-toggle="dropdown" aria-haspopup="true"
					aria-expanded="false"><%=user.isPresent() ? user.get().getEmail() : "-"%>
				</a>
					<div class="dropdown-menu" aria-labelledby="navbarDropdown">
						<a class="dropdown-item" href="/rollflower/cabinet">Profile</a>
						<div class="dropdown-divider"></div>
						<a class="dropdown-item" href="/rollflower/logout">Log out</a>
					</div></li>
			</ul>
		</div>
		</nav>
		<div class="container">
			<div class="row">
				<div class="col-md-6  offset-md-0  toppad" style="margin-top: 50px;">
					<div class="card">
						<div class="card-body">
							<h2 class="card-title">Profile</h2>
							<table class="table table-user-information ">
								<tbody>
									<tr>
										<td>Username:</td>
										<td><%=user.isPresent() ? user.get().getUsername() : "-"%></td>
									</tr>
									<tr>
										<td>Email:</td>
										<td><%=user.isPresent() ? user.get().getEmail() : "-"%></td>
									</tr>
								</tbody>
							</table>
							<button type="button" class="btn btn-danger delete-button"
								data-toggle="tooltip" data-placement="bottom"
								title="Delete account">&#10005;</button>
						</div>
					</div>
				</div>
				<div class="col-md-6  offset-md-0  toppad" style="margin-top: 50px;">
					<div class="card">
						<div class="card-body">
							<h3 class="card-title">Password change:</h3>
							<form id="password-form" method="post" role="form"
								action="/rollflower/cabinet/pw_change">
								<table class="table table-user-information ">
									<tbody>
										<tr>
											<td>Old password:</td>
											<td><input icon="password-icon" name="password"
												type="password" value='' focus /></td>
										</tr>
										<tr>
											<td>New password:</td>
											<td><Input icon="password-icon" name="newPassword"
												type="password" value='' /></td>
										</tr>
										<tr>
											<td>Confirm new password:</td>
											<td><Input icon="password-icon" name="newPasswordConf"
												type="password" value='' /></td>
										</tr>
									</tbody>
								</table>
								<button type="submit" class="btn btn-dark mb-2">Change</button>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>