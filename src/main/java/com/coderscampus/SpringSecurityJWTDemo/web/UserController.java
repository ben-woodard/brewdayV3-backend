package com.coderscampus.SpringSecurityJWTDemo.web;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.coderscampus.SpringSecurityJWTDemo.dto.request.SignUpRequest;
import com.coderscampus.SpringSecurityJWTDemo.dto.response.JwtAuthenticationResponse;
import com.coderscampus.SpringSecurityJWTDemo.domain.User;
import com.coderscampus.SpringSecurityJWTDemo.security.AuthenticationServiceImpl;
import com.coderscampus.SpringSecurityJWTDemo.security.JwtServiceImpl;
import com.coderscampus.SpringSecurityJWTDemo.service.RefreshTokenService;
import com.coderscampus.SpringSecurityJWTDemo.service.impl.UserServiceImpl;

@Controller
public class UserController {

	private UserServiceImpl userService;
	private AuthenticationServiceImpl authenticationService;
	private JwtServiceImpl jwtService;
	private RefreshTokenService refreshTokenService;
	private PasswordEncoder passwordEncoder;
	private Logger logger = LoggerFactory.getLogger(UserController.class);

	
	public UserController(UserServiceImpl userService, AuthenticationServiceImpl authenticationService,
						  JwtServiceImpl jwtService, RefreshTokenService refreshTokenService, PasswordEncoder passwordEncoder) {
		super();
		this.userService = userService;
		this.authenticationService = authenticationService;
		this.jwtService = jwtService;
		this.refreshTokenService = refreshTokenService;
		this.passwordEncoder = passwordEncoder;
	}


	@GetMapping("/register")
	public String getRegistration (ModelMap model) {
		model.addAttribute("user", new User());
		return "registration";
	}
	
	
	@PostMapping("/register")
	public String processRegistration(@ModelAttribute("user") User user, SignUpRequest request) {
	    Optional<User> existingUser = userService.findUserByEmail(user.getEmail());
	    String encodedPassword = passwordEncoder.encode(request.password());
	    

	    if (existingUser.isPresent()) {
	    	logger.error("User already exists. Redirecting to userExists.");
	        // Redirect to the userExists page if a user with the same email exists
	        return "userExists";
	    } else {
	    	JwtAuthenticationResponse signupResponse = authenticationService.signup(request);
	    	
	    	logger.info("Processing registration for user: " + user.getEmail());
	    	logger.info("Provided password during registration: " + request.password());
	    	logger.info("Encoded password during registration: " + encodedPassword);
			
	        if (signupResponse != null) {
	            // Successfully registered user, now proceed with authentication
	                logger.info("Successfully registered user. Redirecting to success.");
	                return "login";
	            } else {
	                // Handle the case where authentication is not successful
	            	logger.error("User registration failed. Redirecting to error.");
	                return "error";
	            }
	        }
	    }
	}
	/*
	 * This code is from Trevor's original implementation which might be helpful for
	 * those who are not using server rendering templates
	 * 
	 * @PostMapping("/signup") 
	 * public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody SignUpRequest request) { 
	 * return ResponseEntity.ok(authenticationService.signup(request)); 
	 * }
	 */


