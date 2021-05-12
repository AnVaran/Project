package application.web.servlets;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import application.model.Role;
import application.model.User;
import application.service.RoleService;
import application.service.UserService;
import application.service.impl.RoleServiceImpl;
import application.service.impl.UserServiceImpl;

public class SecurityRegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(SecurityRegisterServlet.class);
	private UserService userService;
	private RoleService roleService;
	private Pattern emailPattern;
	private Pattern usernamePattern;
	private Pattern passwordPattern;
	private MessageDigest md;

	public SecurityRegisterServlet() {
		super();

	}

	public void init() throws ServletException {
		userService = new UserServiceImpl();
		roleService = new RoleServiceImpl();
		emailPattern = Pattern.compile(
				"^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
		usernamePattern = Pattern.compile("^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$");
		passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,20}$");
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error(e.getMessage());
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect("/rollflower/security");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Optional<String> username = Optional.ofNullable(request.getParameter("username"));
		Optional<String> email = Optional.ofNullable(request.getParameter("email"));
		Optional<String> password = Optional.ofNullable(request.getParameter("password"));
		Optional<String> confirmPassword = Optional.ofNullable(request.getParameter("confirm-password"));
		if (username.isPresent() && email.isPresent() && password.isPresent() && confirmPassword.isPresent()) {
			if (!emailPattern.matcher(email.get()).matches()) {
				request.setAttribute("registrationValidationExeption", "Incorrect email.");
				request.getRequestDispatcher("/securityForm.jsp").forward(request, response);
				return;
			} else if (usernamePattern.matcher(username.get()).matches()) {
				request.setAttribute("registrationValidationExeption", "Incorrect username.");
				request.getRequestDispatcher("/securityForm.jsp").forward(request, response);
				return;
			} else if (passwordPattern.matcher(password.get()).matches()) {
				request.setAttribute("registrationValidationExeption", "Incorrect password.");
				request.getRequestDispatcher("/securityForm.jsp").forward(request, response);
				return;
			} else if (!password.get().equals(confirmPassword.get())) {
				request.setAttribute("registrationValidationExeption", "Passwords don't match.");
				request.getRequestDispatcher("/securityForm.jsp").forward(request, response);
				return;
			}
			Role role = roleService.getAllRoles().stream().filter(c -> c.getRoleName().equals("USER")).findAny().get();
			User user = new User();
			user.setUsername(username.get());
			user.setPassword(password.get());
			user.setEmail(email.get());
			user.setUserRole(role);
			if (userService.userExists(user)) {
				request.setAttribute("registrationValidationExeption", "User allready exists.");
				request.getRequestDispatcher("/securityForm.jsp").forward(request, response);
				return;
			}
			user.setId(userService.addUser(user));
			StringBuilder sb = new StringBuilder();
			for (byte b : md.digest((user.getEmail() + "" + request.getSession(false).getId()).getBytes())) {
				sb.append(String.format("%02x", b));
			}
			user.setSessionKey(sb.toString());
			userService.updateUserSessionKey(user);
			Cookie cookie = new Cookie("session_access_token", user.getSessionKey());
			cookie.setMaxAge(60 * 60 * 2);
			cookie.setPath("/rollflower");
			response.addCookie(cookie);
			response.sendRedirect("/rollflower/cabinet");
		} else {
			response.sendRedirect("/rollflower/security");
		}
	}

}
