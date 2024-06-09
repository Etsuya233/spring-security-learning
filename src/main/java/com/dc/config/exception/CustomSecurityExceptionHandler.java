package com.dc.config.exception;

import com.dc.domain.R;
import com.dc.utils.JSON;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 捕捉Spring security filter chain 中抛出的未知异常
 */
public class CustomSecurityExceptionHandler extends OncePerRequestFilter {

  public static final Logger logger = LoggerFactory.getLogger(
      CustomSecurityExceptionHandler.class);

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      // 未知异常
      R result = R.error(e.getMessage());
      response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
      response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      PrintWriter writer = response.getWriter();
      writer.write(JSON.stringify(result));
      writer.flush();
      writer.close();
    }
  }
}
