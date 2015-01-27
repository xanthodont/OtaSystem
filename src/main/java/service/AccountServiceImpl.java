package service;

import com.google.inject.Inject;

import areas.user.models.Account;
import dao.IBasicDao;
import dao.base.IDatabase;

public class AccountServiceImpl implements IAccountService {
	@Inject
	IDatabase<Account> accountDao;

	@Override
	public boolean validateCredentials(String username, String password) {
		// TODO Auto-generated method stub
		long count = accountDao.count(c -> c.equals("username", username));
		if (count == 1) {
			Account a = accountDao.first(c -> c.equals("username", username)); 
			//System.out.println("password:"+a.getPassword() + " "+password);
			if (a.getPassword().equals(password)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean validateUsernameUnique(String username) {
		// TODO Auto-generated method stub
		long count = accountDao.count(c -> c.equals("username", username));
		return count < 1;
	}
	
	
}
