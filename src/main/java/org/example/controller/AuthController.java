
package org.example.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.example.model.RoleType;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.utility.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthenticationManager authManager;
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;
	private final PasswordEncoder passwordEncoder;

	public AuthController(AuthenticationManager authManager,
			UserRepository repo,
			JwtUtil jwtUtil,
			PasswordEncoder encoder) {
		this.authManager = authManager;
		this.userRepository = repo;
		this.jwtUtil = jwtUtil;
		this.passwordEncoder = encoder;
	}

	@PostMapping("/register")
	public Map<String, String> register(@RequestBody Map<String, Object> body) {
		String username = (String) body.get("username");
		String password = (String) body.get("password");
		List<String> rolesList = (List<String>) body.get("roles");

		if (userRepository.findByUsername(username).isPresent()) {
			throw new RuntimeException("User already exists");
		}

		Set<RoleType> roles = rolesList.stream()
				.map(RoleType::valueOf)
				.collect(Collectors.toSet());

		User user = User.builder()
				.username(username)
				.password(passwordEncoder.encode(password))
				.roles(roles)
				.build();

		userRepository.save(user);

		String token = jwtUtil.generateToken(username);
		return Map.of("jwt", token);
	}

	@PostMapping("/login")
	public Map<String, String> login(@RequestBody Map<String, Object> body) throws Exception {
		String username = (String) body.get("username");
		String password = (String) body.get("password");
		authManager.authenticate(
				new UsernamePasswordAuthenticationToken(username, password));

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found"));

		String token = jwtUtil.generateToken(username);
		return Map.of("jwt", token);
	}
}
