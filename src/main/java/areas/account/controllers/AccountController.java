package areas.account.controllers;


import java.util.List;

import org.slf4j.Logger;

import ninja.Result;
import ninja.Results;
import areas.account.models.Account;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import dao.IBasicDao;

@Singleton
public class AccountController {
	@Inject
	private IBasicDao<Account> dao;
	
	@Inject
	private Logger logger;
	
	public Result login() {
		logger.debug("account/login");
		return Results.html();
	}
	
	public Result register() {
		return Results.html();
	}
	
	public Result profile() {
		List<Account> accountList = dao.all();
		logger.debug("size:%d, username: %s", accountList.size(), accountList.get(0).getUsername());
		return Results.json().render(accountList);
	}
}
