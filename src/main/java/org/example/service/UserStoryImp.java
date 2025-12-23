package org.example.service;

import java.util.List;

import org.example.model.Statut;
import org.example.model.Task;
import org.example.model.UserStory;

public class UserStoryImp implements UserStoryService {

	@Override
	public UserStory getUserStoryById(Long userStoryId) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getUserStoryById'");
	}

	@Override
	public void addUserStory(UserStory userStory) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'addUserStory'");
	}

	@Override
	public Boolean existsByTitle(String title) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'existsByTitle");
	}

	@Override
	public void updateUserStory(UserStory oldUserStory, UserStory newUserStory) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'updateUserStory'");
	}

	@Override
	public void deleteUserStory(UserStory u) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'deleteUserStory'");
	}

	@Override
	public List<Task> getTasks() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getTasks'");
	}

	@Override
	public Statut getStatus(UserStory u) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getStatus'");
	}

	@Override
	public Boolean isAccepted(UserStory u) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'isAccepted'");
	}

}
