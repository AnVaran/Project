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

public class CabinetDeleteAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(CabinetPageServlet.class);
	private UserService userService;
	private MessageDigest md;

	public CabinetDeleteAccountServlet() {

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
		Optional<String> password = Optional.ofNullable(request.getParameter("confirmPassword"));
		HttpSession session = request.getSession(false);
		if (password.isPresent()) {
			Cookie[] cookies = request.getCookies();
			Optional<Cookie> resultCookie = cookies == null ? Optional.empty()
					: Arrays.stream(cookies).filter(p -> p.getName().equals("session_access_token")).findFirst();
			if (resultCookie.isPresent() && session != null) {
				User user = userService.getUserBySessionKey(resultCookie.get().getValue());
				StringBuilder sb = new StringBuilder();
				for (byte b : md.digest((user.getEmail() + "" + session.getId()).getBytes())) {
					sb.append(String.format("%02x", b));
				}
				if (sb.toString().equals(resultCookie.get().getValue()) && user.getPassword().equals(password.get())) {
					userService.deleteUser(user.getId());
					resultCookie.get().setMaxAge(0);
					session.invalidate();
					response.addCookie(resultCookie.get());
					request.setAttribute("responseBody", "Your account has been successfully removed!");
					request.getRequestDispatcher("/component/deleteResult.jsp").forward(request, response);
					return;
				} else {
					resultCookie.get().setMaxAge(0);
					response.addCookie(resultCookie.get());
					request.setAttribute("responseBody", "Please check your data and relog!");
					request.getRequestDispatcher("/component/deleteResult.jsp").forward(request, response);
					return;
				}
			} else {
				doGet(request, response);
			}
		} else {
			doGet(request, response);
		}
	}
}
