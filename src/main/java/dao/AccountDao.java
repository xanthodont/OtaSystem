package dao;

import dao.base.Database;
import ninja.UsernamePasswordValidator;
import areas.user.models.Account;

public class AccountDao extends Database<Account> implements UsernamePasswordValidator {
	
	@Override
	public boolean validateCredentials(String username, String password) {
		// TODO Auto-generated method stub
		//this.get
		return "xanthodont".equals(username);
	}
	
}
