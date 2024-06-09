package com.dc.controller;

import com.dc.domain.LoginDto;
import com.dc.domain.R;
import com.dc.domain.User;
import com.dc.service.IUserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Etsuya
 * @since 2024-06-08
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final IUserService userService;

	@GetMapping("/{id}")
	public R<User> getUserInfo(@PathVariable Long id){
		return R.ok(userService.getById(id));
	}

	@PostMapping("/login")
	public R<String> login(@RequestBody LoginDto loginDto){
		User user = userService.lambdaQuery()
				.eq(User::getUsername, loginDto.getUsername())
				.one();
		if(user == null){
			throw new RuntimeException("用户不存在！");
		}
		if(!user.getPassword().equals(loginDto.getPassword())){
			throw new RuntimeException("账户名或密码错误！");
		}
		return R.ok("12345");
	}


}
