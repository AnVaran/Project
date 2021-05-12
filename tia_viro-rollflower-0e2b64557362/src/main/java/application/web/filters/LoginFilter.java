package application.web.filters;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import application.model.User;
import application.service.UserService;
import application.service.impl.UserServiceImpl;

public class LoginFilter implements Filter {
	private static final Logger LOGGER = Logger.getLogger(LoginFilter.class);
	private UserService userService;
	private MessageDigest md;

	public LoginFilter() {

	}

	public void init(FilterConfig fConfig) throws ServletException {
		userService = new UserServiceImpl();
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error(e.getMessage());
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);
		Cookie[] cookies = req.getCookies();
		Optional<Cookie> resultCookie = cookies == null ? Optional.empty()
				: Arrays.stream(cookies).filter(p -> p.getName().equals("session_access_token")).findAny();
		if (resultCookie.isPresent() && session != null) {
			Optional<User> user = Optional.ofNullable(userService.getUserBySessionKey(resultCookie.get().getValue()));
			if (user.isPresent()) {
				StringBuilder sb = new StringBuilder();
				for (byte b : md.digest((user.get().getEmail() + "" + session.getId()).getBytes())) {
					sb.append(String.format("%02x", b));
				}
				if (sb.toString().equals(resultCookie.get().getValue())) {
					req.setAttribute("user", user.get());
					req.getRequestDispatcher("cabinet").forward(req, res);
				} else {
					resultCookie.get().setMaxAge(0);
					res.addCookie(resultCookie.get());
					chain.doFilter(req, res);
				}
			} else {
				chain.doFilter(request, response);
			}
		} else {
			chain.doFilter(request, response);
		}
	}

}
