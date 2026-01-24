
package org.example.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.example.dto.LoginRequestDto;
import org.example.dto.LoginResponseDto;
import org.example.dto.RegisterRequestDto;
import org.example.model.RoleType;
import org.example.model.User;
import org.example.security.JwtUtil;
import org.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UserService userService;
	private final JwtUtil jwtUtil;
	private final PasswordEncoder passwordEncoder;

	public AuthController(
			UserService service,
			JwtUtil jwtUtil,
			PasswordEncoder encoder) {
		this.userService = service;
		this.jwtUtil = jwtUtil;
		this.passwordEncoder = encoder;
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequestDto request) {
		try {
			String username = (String) request.getUsername();
			String password = (String) request.getPassword();

			// Validation
			if (username == null || username.isBlank()) {
				return ResponseEntity
						.badRequest()
						.body(Map.of("error", "Username is required"));
			}

			if (password == null || password.length() < 8) {
				return ResponseEntity
						.badRequest()
						.body(Map.of("error", "Password must be at least 8 characters"));
			}

			// Check if user exists
			if (userService.userExists(username)) {
				return ResponseEntity
						.status(HttpStatus.CONFLICT)
						.body(Map.of("error", "User already exists"));
			}

			// Create user
			List<String> rolesList = request.getRoles();
			Set<RoleType> roles = rolesList.stream()
					.map(RoleType::valueOf)
					.collect(Collectors.toSet());

			User user = User.builder()
					.username(username)
					.password(passwordEncoder.encode(password))
					.roles(roles)
					.build();

			userService.createUser(user);
			String token = jwtUtil.generateToken(username, roles);

			return ResponseEntity
					.status(HttpStatus.CREATED)
					.body(Map.of("jwt", token));

		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					// .body(Map.of("error", "Registration failed"));
					.body(Map.of("error", e.getMessage()));
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
		try {
			String username = request.getUsername();
			String password = request.getPassword();

			// Check if user exists
			if (!userService.userExists(username)) {
				return ResponseEntity
						.status(HttpStatus.UNAUTHORIZED)
						.body(Map.of("error", "Invalid username or password"));
			}

			// Get user and verify password
			User user = userService.getUserByUsername(username);
			if (!passwordEncoder.matches(password, user.getPassword())) {
				return ResponseEntity
						.status(HttpStatus.UNAUTHORIZED)
						.body(Map.of("error", "Invalid username or password"));
			}

			// Generate token
			String token = jwtUtil.generateToken(username, user.getRoles());

			// Prepare response
			LoginResponseDto response = new LoginResponseDto();
			response.setJwt(token);
			response.setUsername(username);
			response.setRoles(user.getRoles().stream()
					.map(RoleType::name)
					.collect(Collectors.toSet()));

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Login failed"));
		}
	}
}
