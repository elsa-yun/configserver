package com.elsa.configserver.controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestReplacedFilter implements Filter {

	private FilterConfig filterConfig;

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		ServletRequest requestWrapper = null;
		if (request instanceof HttpServletRequest) {
			requestWrapper = new MAPIHttpServletRequestWrapper((HttpServletRequest) request);
		}
		try {
			if (requestWrapper == null) {
				chain.doFilter(request, response);
			} else {
				chain.doFilter(requestWrapper, response);
			}
		} catch (Exception e) {

		} finally {
			if (null != filterConfig.getFilterName()) {
				request.removeAttribute(filterConfig.getFilterName());
			}

		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

}