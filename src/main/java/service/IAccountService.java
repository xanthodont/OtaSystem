package service;

public interface IAccountService {
	boolean validateCredentials(String username, String password); 
	
	boolean validateUsernameUnique(String username);
}
