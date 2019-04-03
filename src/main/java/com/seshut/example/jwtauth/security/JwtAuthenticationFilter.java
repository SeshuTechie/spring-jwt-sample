package com.seshut.example.jwtauth.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.seshut.example.jwtauth.common.WebCommons;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
    private JwtTokenUtil tokenProvider;
	
	@Autowired
    private CustomUserDetailsService customUserDetailsService;
	
	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            
            if (StringUtils.hasText(jwt) ) {
            	if(tokenProvider.validateToken(jwt))
            	{
	                Long userId = tokenProvider.getUserIdFromToken(jwt);
	
	                UserDetails userDetails = customUserDetailsService.loadUserById(userId);
	                if(userDetails != null && SecurityContextHolder.getContext().getAuthentication() == null)
	                {
	                	logger.debug("security context was null, so authorizing user");
		                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		                SecurityContextHolder.getContext().setAuthentication(authentication);
	                }
	                else
	                {
	                	logger.warn("security context has authentication details");
	                }
            	}
            	else
                {
                	logger.warn("token validation failed, will ignore the header");
                }
            }
            else
            {
            	logger.warn("couldn't find token, will ignore the header");
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String token = request.getHeader(WebCommons.ACCESS_TOKEN_KEY);
        return token;
    }
}
