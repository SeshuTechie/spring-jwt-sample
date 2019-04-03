package com.seshut.example.jwtauth.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seshut.example.jwtauth.user.User;
import com.seshut.example.jwtauth.user.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		User userFilter = new User();
		userFilter.setEmail(usernameOrEmail);
		User user = userRepository.findByEmail(usernameOrEmail);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with email : " + usernameOrEmail);
		}
		return CustomUserDetails.create(user);
	}

	@Transactional
	public UserDetails loadUserById(Long id) {

		Optional<User> optional = userRepository.findById(id.intValue());
		if (!optional.isPresent()) {
			throw new UsernameNotFoundException("User not found with id : " + id);
		}
		User user = optional.get();
		
		return CustomUserDetails.create(user);
	}
}
