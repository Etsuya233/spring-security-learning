package com.dc.config;

import com.dc.config.exception.CustomAuthenticationExceptionHandler;
import com.dc.config.exception.CustomAuthorizationExceptionHandler;
import com.dc.config.exception.CustomSecurityExceptionHandler;
import com.dc.config.handler.LoginFailHandler;
import com.dc.config.handler.LoginSuccessHandler;
import com.dc.config.username.UsernameAuthenticationFilter;
import com.dc.config.username.UsernameAuthenticationProvider;
import jakarta.servlet.Filter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final AuthenticationEntryPoint authenticationExceptionHandler = new CustomAuthenticationExceptionHandler();
	private final AccessDeniedHandler authorizationExceptionHandler = new CustomAuthorizationExceptionHandler();
	private final Filter globalSpringSecurityExceptionHandler = new CustomSecurityExceptionHandler();

	private final ApplicationContext applicationContext;

	public SecurityConfig(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		commonHttpSetting(http);

		//配置Filter
		http
				.authorizeHttpRequests(authorize -> authorize
						.anyRequest().authenticated()
				)
				.addFilterBefore(new CustomSecurityFilter(), UsernamePasswordAuthenticationFilter.class);


		// 处理 SpringSecurity 异常响应结果。响应数据的结构，改成业务统一的JSON结构。不要框架默认的响应结构。
		http.exceptionHandling(exceptionHandling ->
				exceptionHandling
						// 认证失败异常
						.authenticationEntryPoint(authenticationExceptionHandler)
						// 鉴权失败异常
						.accessDeniedHandler(authorizationExceptionHandler)
		);
		http.addFilterBefore(globalSpringSecurityExceptionHandler, SecurityContextHolderFilter.class);


		return http.build();
	}

	private static void commonHttpSetting(HttpSecurity http) throws Exception {
		//禁用不需要的Filter
		http.formLogin(AbstractHttpConfigurer::disable)
				.httpBasic(AbstractHttpConfigurer::disable)
				.logout(AbstractHttpConfigurer::disable)
				.sessionManagement(AbstractHttpConfigurer::disable)
				.csrf(AbstractHttpConfigurer::disable)
				// requestCache用于重定向，前后端分析项目无需重定向，requestCache也用不上
				.requestCache(cache -> cache
						.requestCache(new NullRequestCache())
				)
				// 无需给用户一个匿名身份
				.anonymous(AbstractHttpConfigurer::disable);
	}

	@Bean
	public SecurityFilterChain loginFilterChain(HttpSecurity http) throws Exception {
		commonHttpSetting(http);

		// 使用securityMatcher限定当前配置作用的路径
		http.securityMatcher("/user/login/*")
				.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());

		LoginSuccessHandler loginSuccessHandler = applicationContext.getBean(LoginSuccessHandler.class);
		LoginFailHandler loginFailHandler = applicationContext.getBean(LoginFailHandler.class);

		// 加一个登录方式。用户名、密码登录
		UsernameAuthenticationFilter usernameLoginFilter = new UsernameAuthenticationFilter(
				new AntPathRequestMatcher("/user/login/username", HttpMethod.POST.name()),
				new ProviderManager(
						List.of(applicationContext.getBean(UsernameAuthenticationProvider.class))),
				loginSuccessHandler,
				loginFailHandler);
		http.addFilterBefore(usernameLoginFilter, UsernamePasswordAuthenticationFilter.class);

//		// 加一个登录方式。短信验证码 登录
//		SmsAuthenticationFilter smsLoginFilter = new SmsAuthenticationFilter(
//				new AntPathRequestMatcher("/user/login/sms", HttpMethod.POST.name()),
//				new ProviderManager(
//						List.of(applicationContext.getBean(SmsAuthenticationProvider.class))),
//				loginSuccessHandler,
//				loginFailHandler);
//		http.addFilterBefore(smsLoginFilter, UsernamePasswordAuthenticationFilter.class);
//
//
//		// 加一个登录方式。Gitee 登录
//		GiteeAuthenticationFilter giteeFilter = new GiteeAuthenticationFilter(
//				new AntPathRequestMatcher("/user/login/gitee", HttpMethod.POST.name()),
//				new ProviderManager(
//						List.of(applicationContext.getBean(GiteeAuthenticationProvider.class))),
//				loginSuccessHandler,
//				loginFailHandler);
//		http.addFilterBefore(giteeFilter, UsernamePasswordAuthenticationFilter.class);


		return http.build();
	}
}