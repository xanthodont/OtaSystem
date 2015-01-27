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
import areas.user.models.Role;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import controllers.ApplicationController;
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
	@Inject
	private IDatabase<Role> roleDao;
	
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
		List<Role> roles = roleDao.all().toList(); 
		return Results.html().render("roles", roles);
	}
	
	public Result save(Account account, @Param(value="roleId") long roleId) {
		if (accountService.validateUsernameUnique(account.getUsername())) {
			account.setRegisterTime(System.currentTimeMillis());
			account.setRole(roleDao.first(c -> c.equals("id", roleId)));
			accountDao.insert(account).commit();
			return Results.json().render(JResponse.success("/user/account/list"));
		} else {
			return Results.json().render(JResponse.fail(msg.get("account.usernameNotUnique", language).get()));
		}
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
	
	public Result delete(@Param(value="id") String id) {
		Account a = accountDao.first(c -> c.equals("id", id));
		if (a == null) {
			return Results.json().render(JResponse.fail(""));
		} else {
			accountDao.delete(a).commit();
			return Results.json().render(JResponse.success(""));
		}
	}
}
