<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<link rel='stylesheet' type="text/css"
	href="/rollflower/styles/loginStyles.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<link
	href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css"
	rel="stylesheet" id="bootstrap-css">
<script
	src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="/rollflower/scripts/loginPageScripts.js"></script>
<title>Log In</title>
</head>
<body>
	<%
		if (request.getAttribute("loginValidationExeption") != null) {
	%>
	<div class="alert alert-info"
		style="position: fixed; z-index: 100; width: 100%">
		<a class="close" data-dismiss="alert" aria-label="close">&times;</a><strong><%=request.getAttribute("loginValidationExeption")%></strong>
	</div>
	<%
		} else if (request.getAttribute("registrationValidationExeption") != null) {
	%>
	<div class="alert alert-info"
		style="position: fixed; z-index: 100; width: 100%">
		<a class="close" data-dismiss="alert" aria-label="close">&times;</a><strong><%=request.getAttribute("registrationValidationExeption")%></strong>
	</div>
	<%
		}
	%>
	<div class="container">
		<nav class="navbar navbar-default">
			<div class="container-fluid">
				<div class="navbar-header">
					<a class="navbar-brand" href="/rollflower/">RollFlower</a>
				</div>
				
			</div>
		</nav>
	</div>
	<div class="container container-mid">
		<div class="row">
			<div class="col-md-6 col-md-offset-3">
				<div class="panel panel-login">
					<div class="panel-heading">
						<div class="row">
							<%
								if (request.getAttribute("registrationValidationExeption") != null) {
							%>
							<div class="col-xs-6">
								<a href="#" id="login-form-link">Login</a>
							</div>
							<div class="col-xs-6">
								<a href="#" class="active" id="register-form-link">Register</a>
							</div>
							<%
								} else {
							%>
							<div class="col-xs-6">
								<a href="#" class="active" id="login-form-link">Login</a>
							</div>
							<div class="col-xs-6">
								<a href="#" id="register-form-link">Register</a>
							</div>
							<%
								}
							%>
						</div>
						<hr>
					</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-lg-12">
								<%
									if (request.getAttribute("registrationValidationExeption") != null) {
								%>
								<form id="login-form" action="/rollflower/security/login"
									method="post" role="form" style="display: none;">
									<%
										} else {
									%>
									<form id="login-form" action="/rollflower/security/login"
										method="post" role="form" style="display: block;">
										<%
											}
										%>
										<div class="form-group">
											<input name="email" id="email-loggin" tabindex="1"
												class="form-control" placeholder="Email" required>
										</div>
										<div class="form-group">
											<input type="password" name="password" id="password-login"
												tabindex="2" class="form-control" placeholder="Password"
												required>
										</div>
										<div class="form-group">
											<div class="row">
												<div class="col-sm-6 col-sm-offset-3">
													<input type="submit" name="login-submit" id="login-submit"
														tabindex="4" class="form-control btn btn-login"
														value="Log In">
												</div>
											</div>
										</div>
									</form>
									<%
										if (request.getAttribute("registrationValidationExeption") != null) {
									%>
									<form id="register-form" action="/rollflower/security/register"
										method="post" role="form" style="display: block;">
										<%
											} else {
										%>
										<form id="register-form"
											action="/rollflower/security/register" method="post"
											role="form" style="display: none;">
											<%
												}
											%>
											<div class="form-group">
												<input name="username" id="username" tabindex="1"
													class="form-control" placeholder="Username" value=""
													required>
											</div>
											<div class="form-group">
												<input name="email" id="email" tabindex="1"
													class="form-control" placeholder="Email Address" value=""
													required>
											</div>
											<div class="form-group">
												<input type="password" name="password" id="password"
													tabindex="2" class="form-control" placeholder="Password"
													required>
											</div>
											<div class="form-group">
												<input type="password" name="confirm-password"
													id="confirm-password" tabindex="2" class="form-control"
													placeholder="Confirm Password" required>
											</div>
											<div class="form-group">
												<div class="row">
													<div class="col-sm-6 col-sm-offset-3">
														<input type="submit" name="register-submit"
															id="register-submit" tabindex="4"
															class="form-control btn btn-register"
															value="Register Now">
													</div>
												</div>
											</div>
										</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>