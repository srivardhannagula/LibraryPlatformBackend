package com.libraryproject.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.libraryproject.main.entity.AuthRequest;
import com.libraryproject.main.entity.AuthResponse;
import com.libraryproject.main.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/api/auth")
public class AuthenticateUser {
	 
	 @Autowired
	 private AuthService authService;
	 
	 @PostMapping("/register")
	 public ResponseEntity<?> postMethodName(@RequestBody AuthRequest request) {
	 	 authService.register(request);
	 	return ResponseEntity.ok("User Registered");
	 }
	 @PostMapping("/login")
	 public ResponseEntity<?> login(@RequestBody AuthRequest request){
		 String token=authService.login(request);
		 System.out.println("Conntroller token  "+token);
		 return ResponseEntity.ok(new AuthResponse(token));
	 }
}
