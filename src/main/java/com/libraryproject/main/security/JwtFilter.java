package com.libraryproject.main.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
 
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.libraryproject.main.entity.LibraryUser;
import com.libraryproject.main.repositories.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
// FILTER IS USED TO VALIDATE THE REQUEST FROM ROLE AND AUTHENTICATE
@Component
public class JwtFilter extends OncePerRequestFilter {
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private UserRepository userRepository;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		 String authHeader=request.getHeader("Authorization");
		 System.out.println("auth header"+authHeader);
		 String token=null;
		 String username=null;
		 if(authHeader!=null&&authHeader.startsWith("Bearer ")) {
			 token=authHeader.substring(7);
			 username=jwtUtil.extractUsername(token);
			 System.out.println("Username"+username);
		 }
		 if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			 LibraryUser user=userRepository.findById(username).orElseThrow();
			 UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(user.getUsername(), null,List.of(new SimpleGrantedAuthority(user.getRole()+"")));
			 System.out.println("User role"+user.getRole().toString());
			 System.out.println("Authentication Token"+authenticationToken);
			 SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		 	}
			filterChain.doFilter(request, response);
		
	}
	
}
