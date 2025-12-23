package org.example.repository;

import java.util.List;
import java.util.Optional;

import org.example.model.RoleType;
import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);

	List<User> findByRole(RoleType roleType);
}
