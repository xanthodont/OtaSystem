package areas.user.controllers;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.slf4j.Logger;

import service.IAccountService;
import ninja.Result;
import ninja.Results;
import ninja.params.Param;
import ninja.session.FlashScope;
import areas.user.models.Account;
import areas.user.models.Profile;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import dao.AccountDao;
import dao.IBasicDao;
import dao.base.IDatabase;

@Singleton
public class AccountController {
	@Inject
	private IDatabase<Account> accountDao;
	@Inject
	private IAccountService accountService;
	
	
	@Inject
	private Logger logger;
	
	public Result list(FlashScope flashScope) {
		List<Account> accounts = accountDao.all().toList();
		long count = accounts.size();
		flashScope.put("count", count);
		return Results.html().render("accounts", accounts);
	}
	
	public Result login(
			@Param("username") String username, 
			@Param("password") String password) {
		if (accountService.validateCredentials(username, password)) {
			
		} else {
			
		}
		return Results.html();
	}
	
	public Result add() {
		Account a = new Account();
		a.setUsername("a2");
		a.setPassword("123456");
		a.setRegisterTime(System.currentTimeMillis());
		accountDao.insert(a).commit();
		List<Account> entities = accountDao.all().toList();
		//dao.insert(entities.toArray());
		Predicate<Account> p = (Account act) -> act.getUsername().equals("a1");
		Account af = accountDao.first();
		//logger.debug(af.getPassword());
		return Results.json().render("size", entities.size());
	}
	
	public Result register() {
		return Results.html();
	}
	
	public Result profile() {
		return Results.ok();
	}
}
