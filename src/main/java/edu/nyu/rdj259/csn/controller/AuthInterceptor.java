package edu.nyu.rdj259.csn.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class AuthInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String uri = request.getRequestURI();
		if (!uri.endsWith("login") && !uri.endsWith("logout") && !uri.endsWith("authenticate") && !uri.endsWith("register")) {
			String emp = (String) request.getSession().getAttribute("emp");
			if (emp == null || emp.trim().equals("")) {
				response.sendRedirect("/csn/login");
				return false;
			}
		}
		return true;
	}
}
