package com.dc.config.username;

import com.dc.domain.User;
import com.dc.domain.UserSafeInfo;
import com.dc.service.IUserService;
import com.dc.utils.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 帐号密码登录认证
 */
@Component
public class UsernameAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private IUserService userService;

	public UsernameAuthenticationProvider() {
		super();
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// 用户提交的用户名 + 密码：
		String username = (String)authentication.getPrincipal();
		String password = (String) authentication.getCredentials();

		// 查数据库，匹配用户信息
		User user = userService.lambdaQuery()
				.eq(User::getUsername, username)
				.one();
		if (user == null || !password.equals(user.getPassword())){
			// 密码错误，直接抛异常。
			// 根据SpringSecurity框架的代码逻辑，认证失败时，
			// 应该抛这个异常：org.springframework.security.core.AuthenticationException
			// BadCredentialsException就是这个异常的子类
			// 抛出异常后后，AuthenticationFailureHandler的实现类会处理这个异常。
			throw new BadCredentialsException("用户名或密码不正确");
		}

		UsernameAuthentication token = new UsernameAuthentication();
		token.setUser(JSON.convert(user, UserSafeInfo.class));
		token.setAuthenticated(true); // 认证通过，这里一定要设成true
		return token;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.isAssignableFrom(UsernameAuthentication.class);
	}
}

