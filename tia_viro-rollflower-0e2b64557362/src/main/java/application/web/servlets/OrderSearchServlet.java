package application.web.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import application.model.BouquetOrder;
import application.service.OrderService;
import application.service.impl.OrderServiceImpl;

public class OrderSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(OrderSearchServlet.class);
	private OrderService orderService;

	public OrderSearchServlet() {

	}

	public void init() throws ServletException {
		orderService = new OrderServiceImpl();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect("/rollflower/order");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Long orderId = null;
		try {
			orderId = Long.valueOf(request.getParameter("order"));
		} catch (NumberFormatException e) {
			LOGGER.warn(e.getMessage());
			StringBuilder sb = new StringBuilder();
			sb.append("<div class=\"alert alert-light text-center\" role=\"alert\">");
			sb.append("Incorrect order number!");
			sb.append("</div>");
			ServletOutputStream sout = response.getOutputStream();
			sout.print(sb.toString());
			return;
		}
		if (orderService.orderExists(orderId)) {
			BouquetOrder order = orderService.getOrderById(orderId);
			request.setAttribute("order", order);
			request.getRequestDispatcher("/component/orderSearch.jsp").forward(request, response);
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append("<div class=\"alert alert-light text-center\" role=\"alert\">");
			sb.append("There is no such order!");
			sb.append("</div>");
			ServletOutputStream sout = response.getOutputStream();
			sout.print(sb.toString());
		}
	}

}
