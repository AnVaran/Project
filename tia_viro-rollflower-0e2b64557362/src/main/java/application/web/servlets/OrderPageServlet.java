package application.web.servlets;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import application.model.FlowerBouquet;
import application.model.User;
import application.model.component.FlowerComponent;
import application.service.DecorationService;
import application.service.FlowerService;
import application.service.UserService;
import application.service.WrapperService;
import application.service.impl.DecorationServiceImpl;
import application.service.impl.FlowerServiceImpl;
import application.service.impl.UserServiceImpl;
import application.service.impl.WrapperServiceImpl;
import application.util.parser.model.Bouquet;
import application.util.parser.model.Order;

public class OrderPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(OrderPageServlet.class);

	private FlowerService floserService;
	private DecorationService decorationService;
	private WrapperService wrapperService;
	private UserService userService;
	private MessageDigest md;

	public OrderPageServlet() {
		super();

	}

	public void init() throws ServletException {
		floserService = new FlowerServiceImpl();
		decorationService = new DecorationServiceImpl();
		wrapperService = new WrapperServiceImpl();
		userService = new UserServiceImpl();
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error(e.getMessage());
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Optional<Cookie> resultCookie = request.getCookies() == null ? Optional.empty()
				: Arrays.stream(request.getCookies()).filter(p -> p.getName().equals("session_access_token")).findAny();
		if (resultCookie.isPresent() && request.getSession(false) != null) {
			Optional<User> user = Optional.ofNullable(userService.getUserBySessionKey(resultCookie.get().getValue()));
			if (user.isPresent()) {
				StringBuilder sb = new StringBuilder();
				for (byte b : md.digest((user.get().getEmail() + "" + request.getSession(false).getId()).getBytes())) {
					sb.append(String.format("%02x", b));
				}
				if (sb.toString().equals(resultCookie.get().getValue())
						&& user.get().getUserRole().getRoleName().equals("STUFF")) {
					request.setAttribute("orderSearch", true);
					request.getRequestDispatcher("/order.jsp").forward(request, response);
					return;
				} else if (sb.toString().equals(resultCookie.get().getValue())
						&& !user.get().getUserRole().getRoleName().equals("STUFF")) {
					request.setAttribute("user", user.get());
					request.setAttribute("message", "Not enought authority.");
					request.getRequestDispatcher("/cabinet").forward(request, response);
					return;
				}
			} else {
				request.setAttribute("loginValidationExeption",
						"Order information available for stuff only. Please, find our specialist or login.");
				request.getRequestDispatcher("/security").forward(request, response);
				return;
			}
			request.setAttribute("loginValidationExeption",
					"Order information available for stuff only. Please, find our specialist or login.");
			request.getRequestDispatcher("/security").forward(request, response);
			return;
		} else {
			request.setAttribute("loginValidationExeption",
					"Order information available for stuff only. Please, find our specialist or login.");
			request.getRequestDispatcher("/security").forward(request, response);
			return;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getParameter("order") != null) {
			String orderJson = request.getParameter("order");
			if (orderJson != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				Order order = objectMapper.readValue(orderJson, Order.class);
				List<FlowerBouquet> bouquets = new ArrayList<FlowerBouquet>();
				for (Bouquet parsedBouquet : order.getBouquets()) {
					FlowerBouquet bouquet = new FlowerBouquet();
					List<FlowerComponent> components = new ArrayList<FlowerComponent>();
					for (application.util.parser.model.Flower flower : parsedBouquet.getFlowers()) {
						FlowerComponent fComponent = new FlowerComponent();
						fComponent.setFlowerQuantyty(flower.getFlowerQuantity());
						fComponent.setFlowerItem(floserService.getFlowerById(flower.getFlowerId()));
						components.add(fComponent);
					}
					bouquet.setDecorationComponent(parsedBouquet.getDecorationId() == 0 ? null
							: decorationService.getDecorationById(parsedBouquet.getDecorationId()));
					bouquet.setWrapperComponent(parsedBouquet.getWrapperId() == 0 ? null
							: wrapperService.getWrapperById(parsedBouquet.getWrapperId()));
					bouquet.setFlowerComponent(components);
					bouquets.add(bouquet);
				}
				request.setAttribute("order", bouquets);
				HttpSession session = request.getSession();
				session.setAttribute("orderInfo", bouquets);
				request.setAttribute("orderSubmit", true);
				request.getRequestDispatcher("/order.jsp").forward(request, response);
			} else {
				doGet(request, response);
			}
		}
	}
}