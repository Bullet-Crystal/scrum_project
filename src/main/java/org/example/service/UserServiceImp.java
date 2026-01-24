package org.example.service;

import java.util.List;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserServiceImp implements UserService {

	private final UserRepository userRepository;

	public User createUser(User user) {
		return userRepository.save(user); // INSERT
	}

	public User getUserById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found"));
	}

	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found"));
	}

	public Boolean userExists(String username) {
		return userRepository.findByUsername(username).isPresent();
	}

	public List<User> getAllUsers() {
		return userRepository.findAll(); // SELECT * FROM user
	}

	public void deleteUser(Long id) {
		userRepository.deleteById(id); // DELETE
	}
}
