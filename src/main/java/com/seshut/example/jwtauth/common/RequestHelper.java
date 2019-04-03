package com.seshut.example.jwtauth.common;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.seshut.example.jwtauth.security.JwtTokenUtil;
import com.seshut.example.jwtauth.user.User;
import com.seshut.example.jwtauth.user.UserRepository;

@Component
public class RequestHelper {

	@Autowired
    JwtTokenUtil tokenProvider;

	@Autowired
	private UserRepository userRepository;

	public User getUser(HttpServletRequest request)
	{
		User user = null;
		
		String accessToken = request.getHeader("X-Access-Token");
		Long userId = tokenProvider.getUserIdFromToken(accessToken);
		if(userId != null)
		{
			Optional<User> optional = userRepository.findById(userId.intValue());
			if(optional.isPresent())
			{
				user = optional.get();
			}
		}
		return user;
		
	}
}
