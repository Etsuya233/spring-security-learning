package com.dc.config;

import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

@Slf4j
public class CustomSecurityFilter implements Filter {
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		log.error("---My custom filter---");

		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication authentication =
				new TestingAuthenticationToken("username", "password", "ROLE_USER");
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);

		filterChain.doFilter(servletRequest, servletResponse);
	}
}
