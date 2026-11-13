package org.example.user;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.example.common.ConflictException;
import org.example.security.JwtService;
import org.example.user.dto.AuthResponse;
import org.example.user.dto.LoginRequest;
import org.example.user.dto.RegisterRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new ConflictException("Username is already taken");
        }

        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .roles(new HashSet<>(Set.of(RoleType.DEVELOPER)))
                .build();

        User saved = userRepository.save(user);
        return toAuthResponse(saved);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return toAuthResponse(user);
    }

    private AuthResponse toAuthResponse(User user) {
        String token = jwtService.generateToken(user.getUsername(), user.getRoles());
        Set<String> roles = user.getRoles().stream().map(Enum::name).collect(Collectors.toSet());
        return new AuthResponse(token, user.getUsername(), roles);
    }
}
