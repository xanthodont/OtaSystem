package areas.user.controllers;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.slf4j.Logger;

import service.IAccountService;
import models.JResponse;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.i18n.Lang;
import ninja.i18n.Messages;
import ninja.params.Param;
import ninja.session.FlashScope;
import ninja.session.Session;
import areas.user.models.Account;
import areas.user.models.Profile;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import dao.AccountDao;
import dao.IBasicDao;
import dao.base.IDatabase;
import filters.AuthorizationFilter;
import filters.PrivilegeFilter;

@Singleton
public class AccountController {
	@Inject
	private IDatabase<Account> accountDao;
	@Inject
	private IAccountService accountService;
	
	private Messages msg;
	private Optional<String> language;
	private Optional<Result> optResult;
	//private List<String> contentNavs;
	
	@Inject
	public AccountController(Messages msg) {
		this.msg = msg;

		language = Optional.of("zh-CN");
        optResult = Optional.absent();
        
        //contentNavs = new ArrayList<String>();
        //contentNavs.add(msg.get("nav.userAuthority", language).get());
        //session.put("contentNavs", contentNavs);
	}
	
	@Inject
	private Logger logger;
	
	
	
	public Result login(
			@Param("username") String username, 
			@Param("password") String password,
			Session session) {
		if (accountService.validateCredentials(username, password)) {
			session.put(AuthorizationFilter.USERNAME, username);
			return Results.json().render(JResponse.success("/admin"));
		} else {
			return Results.json().render(JResponse.fail(msg.get("login.notice.fail", language).get()));
		}
	}
	
	public Result logout(Session session) {
		session.remove(AuthorizationFilter.USERNAME);
		return Results.redirect("/login");
	}
	
	@FilterWith(AuthorizationFilter.class)
	public Result list(FlashScope flashScope) {
		List<Account> accounts = accountDao.all().toList();
		long count = accounts.size();
		flashScope.put("count", count);
		return Results.html().render("accounts", accounts);
	}
	
	public Result edit() {
		return Results.html();
	}
	
	@FilterWith(AuthorizationFilter.class)
	public Result profile() {
		
		return Results.html();
	}
	
	@FilterWith({
		AuthorizationFilter.class,
		PrivilegeFilter.class})
	public Result add() {
		return Results.redirect("/user/account/edit"); 
	}
	
	public Result register() {
		return Results.html();
	}
}
