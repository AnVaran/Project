package application.web.servlets;

import java.io.IOException;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import application.model.Decoration;
import application.model.Flower;
import application.model.Wrapper;
import application.service.DecorationService;
import application.service.FlowerService;
import application.service.WrapperService;
import application.service.impl.DecorationServiceImpl;
import application.service.impl.FlowerServiceImpl;
import application.service.impl.WrapperServiceImpl;

public class MainPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private FlowerService floserService;
	private DecorationService decorationService;
	private WrapperService wrapperService;

	public MainPageServlet() {
		super();

	}

	public void init(ServletConfig config) throws ServletException {
		floserService = new FlowerServiceImpl();
		decorationService = new DecorationServiceImpl();
		wrapperService = new WrapperServiceImpl();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<Long, Flower> flowerMap = floserService.getAllFlowers().stream()
				.collect(Collectors.toMap(Flower::getId, Function.identity()));
		Map<Long, Decoration> decorationMap = decorationService.getAllDecorations().stream()
				.collect(Collectors.toMap(Decoration::getId, Function.identity()));
		Map<Long, Wrapper> wrapperMap = wrapperService.getAllWrappers().stream()
				.collect(Collectors.toMap(Wrapper::getId, Function.identity()));
		request.setAttribute("flowers", flowerMap);
		request.setAttribute("wrappers", wrapperMap);
		request.setAttribute("decorations", decorationMap);
		request.getRequestDispatcher("/main.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
