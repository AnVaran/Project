package application.web.servlets;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import application.model.User;
import application.service.UserService;
import application.service.impl.UserServiceImpl;

public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UserService userService;

	public LogoutServlet() {
		super();
	}

	public void init() throws ServletException {
		userService = new UserServiceImpl();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Cookie[] cookies = request.getCookies();
		HttpSession session = request.getSession(false);
		Optional<Cookie> resultCookie = cookies == null ? Optional.empty()
				: Arrays.stream(cookies).filter(p -> p.getName().equals("session_access_token")).findFirst();
		if (resultCookie.isPresent()) {
			User user = userService.getUserBySessionKey(resultCookie.get().getValue());
			user.setSessionKey(null);
			userService.updateUserSessionKey(user);
			session.invalidate();
			resultCookie.get().setMaxAge(0);
			response.addCookie(resultCookie.get());
			
		}
		response.sendRedirect("/rollflower/security");
	}

}
