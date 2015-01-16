package dao;

import ninja.UsernamePasswordValidator;
import areas.account.models.Account;

public class AccountDao extends BasicHibernateDao<Account, Long> implements UsernamePasswordValidator {
	
	@Override
	public boolean validateCredentials(String username, String password) {
		// TODO Auto-generated method stub
		//this.get
		return "xanthodont".equals(username);
	}
	
}
