package com.seshut.example.jwtauth.user;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.seshut.example.jwtauth.common.WebCommons;
import com.seshut.example.jwtauth.exception.AppBusinessException;
import com.seshut.example.jwtauth.exception.ExceptionDetails;
import com.seshut.example.jwtauth.exception.InvalidTokenException;
import com.seshut.example.jwtauth.exception.UserAlreadyExists;
import com.seshut.example.jwtauth.payload.user.LoginRequest;
import com.seshut.example.jwtauth.payload.user.RefreshRequest;
import com.seshut.example.jwtauth.payload.user.RefreshTokenResponse;
import com.seshut.example.jwtauth.payload.user.SignupRequest;
import com.seshut.example.jwtauth.payload.user.SignupResponse;
import com.seshut.example.jwtauth.payload.user.UserInfoResponse;
import com.seshut.example.jwtauth.util.PasswordValidator;

@RestController
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	private ModelMapper modelMapper;

	
	@PostMapping(WebCommons.PATH_USERS)
	public ResponseEntity<SignupResponse> createUser(@Valid @RequestBody SignupRequest signupRequest)
	{
		//doing local validation for password
		if(!PasswordValidator.isValid(signupRequest.getPassword()))
		{
			throw new AppBusinessException("Invalid Password.  Use at least 8 characters, including 1 uppercase letter, 1 lowercase letter, and 1 number");
		}
		User user = modelMapper.map(signupRequest, User.class);
		TokenPair tokenPair = userService.createUser(user);
		
		URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path(WebCommons.PATH_USERS)
                .build().toUri();
		SignupResponse signupResponse = new SignupResponse(tokenPair.getAccessToken(), tokenPair.getRefreshToken());
        return ResponseEntity.created(location).body(signupResponse);
	}

	@PostMapping(WebCommons.PATH_ACCESS_TOKENS)
	public ResponseEntity<SignupResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest)
	{
		User user = modelMapper.map(loginRequest, User.class);
		
		TokenPair tokenPair = userService.login(user);
		URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path(WebCommons.PATH_ACCESS_TOKENS)
                .build().toUri();
		SignupResponse signupResponse = new SignupResponse(tokenPair.getAccessToken(), tokenPair.getRefreshToken());
        return ResponseEntity.created(location).body(signupResponse);
	}
	
	@PostMapping(WebCommons.PATH_ACCESS_TOKENS_REFRESH)
	public ResponseEntity<RefreshTokenResponse> refresh(@Valid @RequestBody RefreshRequest refreshRequest)
	{
		TokenPair tokenPair = userService.refersh(refreshRequest.getRefreshToken());
		
		return ResponseEntity.ok().body(new RefreshTokenResponse(tokenPair.getAccessToken()));
	}
	
	@DeleteMapping(WebCommons.PATH_ACCESS_TOKENS)
	public ResponseEntity<SignupResponse> logout(HttpServletRequest request, HttpServletResponse response)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){    
           new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        SecurityContextHolder.getContext().setAuthentication(null);

        //TODO implement in-memory Blacklist for logged out tokens (one way of implementing logout for JWT based security)
        
        return ResponseEntity.noContent().build();
	}
	
	@GetMapping(WebCommons.USER_INFO)
	public ResponseEntity<UserInfoResponse> getUserInfo(HttpServletRequest request, HttpServletResponse response)
	{
		
		String accessToken = request.getHeader(WebCommons.ACCESS_TOKEN_KEY);
		UserInfo userInfo = userService.getUserInfo(accessToken);
		
		return ResponseEntity.ok().body(modelMapper.map(userInfo, UserInfoResponse.class));
	}


	@ExceptionHandler({UserAlreadyExists.class})
    public ResponseEntity<ExceptionDetails> handleAuthenticationException(UserAlreadyExists e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ExceptionDetails(e.getMessage(), null));
	}
	
	@ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<ExceptionDetails> handleAuthenticationException(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDetails(e.getMessage(), null));
	}

	@ExceptionHandler({InvalidTokenException.class})
    public ResponseEntity<ExceptionDetails> handleAuthenticationException(InvalidTokenException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ExceptionDetails(e.getMessage(), null));
	}

	@ExceptionHandler({AppBusinessException.class})
    public ResponseEntity<ExceptionDetails> handleAppBusinessException(AppBusinessException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDetails(e.getMessage(), null));
	}
}
