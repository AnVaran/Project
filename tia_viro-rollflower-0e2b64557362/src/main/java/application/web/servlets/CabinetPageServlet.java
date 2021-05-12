package application.web.servlets;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import application.model.User;

public class CabinetPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public CabinetPageServlet() {

	}

	public void init() throws ServletException {

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Optional<User> user = Optional.ofNullable((User) request.getAttribute("user"));
		Optional<Boolean> authority = Optional.ofNullable((Boolean) request.getAttribute("authority"));
		if (user.isPresent()) {
			request.setAttribute("user", user.get());
		}
		if (authority.isPresent()) {
			request.setAttribute("authority", authority.get());
		}
		request.getRequestDispatcher("/cabinet.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
