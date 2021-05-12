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

import application.model.User;
import application.service.UserService;
import application.service.impl.UserServiceImpl;

public class SecurityLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(SecurityLoginServlet.class);
	private UserService userService;
	private Pattern emailPattern;
	private MessageDigest md;

	public SecurityLoginServlet() {

	}

	public void init() throws ServletException {
		userService = new UserServiceImpl();
		emailPattern = Pattern.compile(
				"^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
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
		Optional<String> email = Optional.ofNullable(request.getParameter("email"));
		Optional<String> password = Optional.ofNullable(request.getParameter("password"));
		if (email.isPresent() && password.isPresent()) {
			if (!emailPattern.matcher(email.get()).matches()) {
				request.setAttribute("loginValidationExeption", "Incorrect email.");
				request.getRequestDispatcher("/securityForm.jsp").forward(request, response);
				return;
			}
			Optional<User> user = Optional.ofNullable(userService.getUserByEmail(email.get()));
			if (user.isPresent() && user.get().getPassword().equals(password.get())) {
				StringBuilder sb = new StringBuilder();
				for (byte b : md.digest((user.get().getEmail() + "" + request.getSession(false).getId()).getBytes())) {
					sb.append(String.format("%02x", b));
				}
				user.get().setSessionKey(sb.toString());
				userService.updateUserSessionKey(user.get());
				Cookie cookie = new Cookie("session_access_token", user.get().getSessionKey());
				cookie.setMaxAge(60 * 60 * 2);
				cookie.setPath("/rollflower");
				response.addCookie(cookie);
				response.sendRedirect("/rollflower/cabinet");
				return;
			} else if (user.isPresent() && !user.get().getPassword().equals(password.get())) {
				request.setAttribute("loginValidationExeption", "Check password");
				request.getRequestDispatcher("/securityForm.jsp").forward(request, response);
				return;
			} else {
				request.setAttribute("loginValidationExeption", "There is no such user.");
				request.getRequestDispatcher("/securityForm.jsp").forward(request, response);
				return;
			}
		} else {
			doGet(request, response);
		}
	}

}
