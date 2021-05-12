package application.web.servlets;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import application.model.User;
import application.service.UserService;
import application.service.impl.UserServiceImpl;

public class CabinetChangePasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(CabinetChangePasswordServlet.class);
	private UserService userService;
	private MessageDigest md;

	public CabinetChangePasswordServlet() {
		super();
	}

	public void init() throws ServletException {
		userService = new UserServiceImpl();
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error(e.getMessage());
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect("/rollflower/cabinet");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Optional<String> oldPassword = Optional.ofNullable(request.getParameter("password"));
		Optional<String> newPassword = Optional.ofNullable(request.getParameter("newPassword"));
		Optional<String> newPasswordConf = Optional.ofNullable(request.getParameter("newPasswordConf"));
		if (oldPassword.isPresent() && newPassword.isPresent() && newPasswordConf.isPresent()) {
			if (!newPassword.get().equals(newPasswordConf.get())) {
				request.setAttribute("message", "New passwor mismatch.");
				request.getRequestDispatcher("/cabinet.jsp").forward(request, response);
				return;
			}
			Cookie[] cookies = request.getCookies();
			HttpSession session = request.getSession(false);
			Optional<Cookie> resultCookie = cookies == null ? Optional.empty()
					: Arrays.stream(cookies).filter(p -> p.getName().equals("session_access_token")).findFirst();
			if (resultCookie.isPresent() && session != null) {
				User user = userService.getUserBySessionKey(resultCookie.get().getValue());
				StringBuilder sb = new StringBuilder();
				for (byte b : md.digest((user.getEmail() + "" + session.getId()).getBytes())) {
					sb.append(String.format("%02x", b));
				}
				if (sb.toString().equals(resultCookie.get().getValue())
						&& user.getPassword().equals(oldPassword.get())) {
					user.setPassword(newPassword.get());
					userService.updateUser(user);
					request.setAttribute("message", "Password has been successfully updated.");
					request.setAttribute("user", user);
					request.getRequestDispatcher("/cabinet.jsp").forward(request, response);
					return;
				} else {
					resultCookie.get().setMaxAge(0);
					response.addCookie(resultCookie.get());
					doGet(request, response);
				}
			} else {
				doGet(request, response);
			}
		} else {
			doGet(request, response);
		}
	}

}
