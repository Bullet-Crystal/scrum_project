package org.example.user;

import org.example.user.dto.AuthResponse;
import org.example.user.dto.LoginRequest;
import org.example.user.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
