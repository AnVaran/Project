package application.web.servlets;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import application.enums.OrderStatusEnum;
import application.model.BouquetOrder;
import application.model.FlowerBouquet;
import application.service.OrderService;
import application.service.impl.OrderServiceImpl;

public class OrderSubmitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private OrderService orderService;
	private Pattern phonePattern;

	public OrderSubmitServlet() {

	}

	public void init() throws ServletException {
		orderService = new OrderServiceImpl();
		phonePattern = Pattern.compile("\\d{9}|\\(\\d{2}\\)\\d{2}-?\\d{2}-?\\d{3}|\\(\\d{2}\\)\\d{4}-?\\d{3}");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect("/rollflower/order");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Optional<String> address = Optional.ofNullable(request.getParameter("address"));
		Optional<String> phone = Optional.ofNullable(request.getParameter("phone"));
		Optional<String> commentary = Optional.ofNullable(request.getParameter("commentary"));
		if (address.isPresent() && phone.isPresent()) {
			if (!phonePattern.matcher(phone.get()).matches()) {
				String message = "Enetered phone number format is unapproved. Please enter your phone like: (XX)XX-XX-XXX, (XX)XX-XXXXX, (XX)XXXXXXX or XXXXXXXXX.";
				request.setAttribute("validationExeption", message);
				request.getRequestDispatcher("/component/orderSubmitResult.jsp").forward(request, response);
				return;
			}
			HttpSession session = request.getSession();
			List<FlowerBouquet> bouquetList = (List<FlowerBouquet>) session.getAttribute("orderInfo");
			BouquetOrder order = new BouquetOrder();
			order.setBouquets(bouquetList);
			order.setShippingAdres(address.get() + ", " + phone.get());
			order.setOrderStatus(OrderStatusEnum.PENDING);
			Long orderId = orderService.addOrder(order);
			request.setAttribute("orderId", orderId);
			request.getRequestDispatcher("/component/orderSubmitResult.jsp").forward(request, response);
			return;
		} else {
			String message = "Phone and address fields are required.";
			request.setAttribute("validationExeption", message);
			request.getRequestDispatcher("/component/orderSubmitResult.jsp").forward(request, response);
			return;
		}
	}
}
