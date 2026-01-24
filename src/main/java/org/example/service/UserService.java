package org.example.service;

import java.util.List;

import org.example.model.User;

public interface UserService {

	public User createUser(User user);

	public User getUserById(Long id);

	public User getUserByUsername(String username);

	public Boolean userExists(String username);

	public List<User> getAllUsers();

	public void deleteUser(Long id);
}
