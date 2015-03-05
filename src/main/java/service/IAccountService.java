package service;

import ninja.session.Session;

public interface IAccountService {
	boolean validateCredentials(String username, String password, Session session); 
	
	boolean validateUsernameUnique(String username);
}
