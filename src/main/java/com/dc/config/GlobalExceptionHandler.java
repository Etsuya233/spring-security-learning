package com.dc.config;

import com.dc.domain.LoginDto;
import com.dc.domain.R;
import com.dc.exception.LoginFailedException;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
	public R<Void> runtimeExceptionHandler(Exception ex, HttpServletResponse response) {
		return R.error(ex.getMessage());
	}

	@ExceptionHandler(LoginFailedException.class)
	public R<Void> loginFailedException(LoginFailedException ex) {
		return R.error(401, ex.getMessage());
	}

}
