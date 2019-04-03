package com.seshut.example.jwtauth.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.seshut.example.jwtauth.exception.InvalidTokenException;
import com.seshut.example.jwtauth.exception.UserAlreadyExists;
import com.seshut.example.jwtauth.security.JwtTokenUtil;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
    JwtTokenUtil tokenUtil;
	
	@Autowired
    AuthenticationManager authenticationManager;
	
	public TokenPair createUser(User user)
	{
		if(userRepository.findByEmail(user.getEmail()) != null) {
            throw new UserAlreadyExists("User already exists by email: " + user.getEmail());
        }
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		User savedUser = userRepository.save(user);
		
		return new TokenPair(tokenUtil.generateToken(savedUser.getId()), tokenUtil.generateRefreshToken(savedUser.getId()));
	}
	
	public TokenPair login(User user)
	{
		User foundUser = userRepository.findByEmail(user.getEmail());
		if(foundUser == null)
		{
			throw new UsernameNotFoundException("User not found: " + user.getEmail());
		}

		Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                		user.getEmail(),
                		user.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new TokenPair(tokenUtil.generateToken(authentication), tokenUtil.generateRefreshToken(authentication));
	}
	
	public TokenPair refersh(String refreshToken)
	{
		String newAccessToken = null;
		
		if(tokenUtil.isRefreshToken(refreshToken) && tokenUtil.validateToken(refreshToken))
		{
			Long userId = tokenUtil.getUserIdFromToken(refreshToken);
			if(userId != null)
			{
				Optional<User> optional = userRepository.findById(userId.intValue());
				if(optional.isPresent())
				{
					newAccessToken = tokenUtil.refreshToken(refreshToken);
				}
				else
				{
					throw new UsernameNotFoundException("InvalidToken. User Not found by id " + userId);
				}
			}
			else
			{
				throw new InvalidTokenException("InvalidToken. Could not get user reference");
			}
		}
		else
		{
			throw new InvalidTokenException("InvalidToken. Could not refresh");
		}
		
		return new TokenPair(newAccessToken, null);
	}

	public UserInfo getUserInfo(String accessToken) {
		UserInfo userInfo = null;
		
		Long userId = tokenUtil.getUserIdFromToken(accessToken);
		if(userId != null)
		{
			Optional<User> optional = userRepository.findById(userId.intValue());
			if(optional.isPresent())
			{
				User user = optional.get();
				userInfo = new UserInfo();
				userInfo.setEmail(user.getEmail());
				userInfo.setName(user.getName());
			}
			else
			{
				throw new UsernameNotFoundException("InvalidToken. User Not found by id " + userId);
			}
		}
		else
		{
			throw new UsernameNotFoundException("InvalidToken. User Not found by id " + userId);
		}
		return userInfo;
	}
}
