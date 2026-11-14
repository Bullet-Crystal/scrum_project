package org.example.user;

import java.util.Set;
import java.util.stream.Collectors;

import org.example.user.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        Set<String> roles = user.getRoles() == null
                ? Set.of()
                : user.getRoles().stream().map(Enum::name).collect(Collectors.toSet());
        return new UserResponse(user.getId(), user.getUsername(), roles);
    }
}
