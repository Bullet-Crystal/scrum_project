package org.example.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import org.example.common.ConflictException;
import org.example.security.JwtService;
import org.example.user.dto.AuthResponse;
import org.example.user.dto.LoginRequest;
import org.example.user.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void register_assignsDeveloperRole_andReturnsToken() {
        RegisterRequest request = new RegisterRequest("alice", "password123");
        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });
        when(jwtService.generateToken("alice", Set.of(RoleType.DEVELOPER))).thenReturn("token");

        AuthResponse response = authService.register(request);

        assertThat(response.token()).isEqualTo("token");
        assertThat(response.roles()).containsExactly("DEVELOPER");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getRoles()).containsExactly(RoleType.DEVELOPER);
        assertThat(captor.getValue().getPassword()).isEqualTo("hashed");
    }

    @Test
    void register_rejectsDuplicateUsername() {
        when(userRepository.existsByUsername("alice")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(new RegisterRequest("alice", "password123")))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void login_rejectsUnknownUser() {
        when(userRepository.findByUsername("alice")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(new LoginRequest("alice", "password123")))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void login_rejectsWrongPassword() {
        User user = User.builder().username("alice").password("hashed").roles(Set.of(RoleType.DEVELOPER)).build();
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong-password", "hashed")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(new LoginRequest("alice", "wrong-password")))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void login_returnsTokenForValidCredentials() {
        User user = User.builder().username("alice").password("hashed").roles(Set.of(RoleType.DEVELOPER)).build();
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "hashed")).thenReturn(true);
        when(jwtService.generateToken("alice", user.getRoles())).thenReturn("token");

        AuthResponse response = authService.login(new LoginRequest("alice", "password123"));

        assertThat(response.token()).isEqualTo("token");
        assertThat(response.username()).isEqualTo("alice");
    }
}
