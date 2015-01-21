package service;

import com.google.inject.Inject;

import areas.account.models.Account;
import dao.IBasicDao;
import dao.base.IDatabase;

public class AccountServiceImpl implements IAccountService {
	@Inject
	IBasicDao<Account, String> accountDao;

	@Override
	public boolean validateCredentials(String username, String password) {
		// TODO Auto-generated method stub
		accountDao.findAll();
		return false;
	}
	
	
}
