package com.libraryproject.main.service;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.libraryproject.main.entity.AuthRequest;
import com.libraryproject.main.entity.LibraryUser;
import com.libraryproject.main.entity.Role;
import com.libraryproject.main.repositories.UserRepository;
import com.libraryproject.main.security.JwtUtil;

@Service
public class AuthService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private JwtUtil jwtUtil;
	
	public void register(AuthRequest request) {
		 
		 LibraryUser user=new LibraryUser();
		 if(userRepository.existsById(request.username)) {
			 throw new RuntimeException("Username already exists");
		 }
		 user.setUsername(request.username.toUpperCase());
		 user.setPassword(bCryptPasswordEncoder.encode(request.password));
		 System.out.println(request.role);
		 user.setRole(Role.valueOf(request.role));
		 userRepository.save(user);
	}
	
	public String login(AuthRequest request) {
		LibraryUser user =userRepository.findById(request.username.toUpperCase()).orElseThrow();
		if(bCryptPasswordEncoder.matches(request.password,user.getPassword())) {
			return jwtUtil.generateToken(user.getUsername(),user.getRole());
		}
		throw	new RuntimeException("Invalid Credentials");
	}
}
