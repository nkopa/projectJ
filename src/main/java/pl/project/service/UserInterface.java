package pl.project.service;

import pl.project.domain.JUser;

public interface UserInterface {
	JUser createUser(JUser user);
	JUser getUserById(Long id);
	JUser findUserByName(String username);
	boolean confirmPassword(String password, JUser user);
	boolean confirmUser(String username);
}
