package org.example.user;

import org.example.user.dto.UserResponse;

public interface UserService {

    User getByUsername(String username);

    User getById(Long id);

    boolean existsByUsername(String username);

    UserResponse getCurrentUser(String username);
}
