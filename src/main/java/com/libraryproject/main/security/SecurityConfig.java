package com.libraryproject.main.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {
		@Autowired
		private JwtFilter jwtFilter;
		@Bean
		public SecurityFilterChain filterChain(HttpSecurity http)
				throws Exception{
			http
			.cors(Customizer.withDefaults())
			.csrf().disable()
			.authorizeHttpRequests(auth->auth
			.requestMatchers("/api/auth/**").permitAll()
			.requestMatchers("/api/admin/**").hasRole("ADMIN")
			.requestMatchers("/api/user/**").hasRole("USER")
			.anyRequest().authenticated())
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			
			http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
			return http.build();
		}
	
		@Bean
		public CorsConfigurationSource corsConfigurationSource() {
		    CorsConfiguration config = new CorsConfiguration();
		    config.setAllowedOrigins(List.of("http://localhost:3000")); // 👈 React frontend origin
		    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		    config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
		    config.setAllowCredentials(true);
		    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		    source.registerCorsConfiguration("/**", config);
		    return source;
		}
	
		
		@Bean
		public BCryptPasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}
		
		@Bean
		public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
				 throws Exception{
			return configuration.getAuthenticationManager();
		}
}
