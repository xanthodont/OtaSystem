package areas.account.controllers;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.slf4j.Logger;

import service.IAccountService;
import ninja.Result;
import ninja.Results;
import ninja.params.Param;
import areas.account.models.Account;
import areas.account.models.Profile;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import dao.AccountDao;
import dao.IBasicDao;
import dao.base.IDatabase;

@Singleton
public class AccountController {
	@Inject
	private IDatabase<Account> dao;
	@Inject
	private IAccountService accountService;
	@Inject
	private IBasicDao<Account, String> accountDao;
	
	
	@Inject
	private Logger logger;
	
	public Result list() {
		//List<Account> entities = accountDao.findAll();
		
		return Results.html();
	}
	
	public Result login() {
		//dao.validateCredentials(username, password);
		return Results.html();
	}
	
	public Result add() {
		Account a = new Account();
		a.setUsername("a2");
		a.setPassword("123456");
		a.setRegisterTime(System.currentTimeMillis());
		dao.insert(a).commit();
		List<Account> entities = dao.all().toList();
		//dao.insert(entities.toArray());
		Predicate<Account> p = (Account act) -> act.getUsername().equals("a1");
		Account af = dao.first((Account act) -> act.getUsername().equals("a1"));
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
