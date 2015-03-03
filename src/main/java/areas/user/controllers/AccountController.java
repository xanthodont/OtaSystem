package areas.user.controllers;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.slf4j.Logger;

import service.IAccountService;
import models.JResponse;
import models.PageList;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.Router;
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
import controllers.BaseController;
import dao.AccountDao;
import dao.IBasicDao;
import dao.base.IDatabase;
import dao.base.IQueryable;
import filters.AuthorizationFilter;
import filters.PrivilegeFilter;

@FilterWith(AuthorizationFilter.class)
public class AccountController extends BaseController {
	@Inject
	private IDatabase<Account> accountDao;
	@Inject
	private IAccountService accountService;
	@Inject
	private IDatabase<Role> roleDao;
	
	
	@Inject
	public AccountController(Messages msg) {
		super(msg);
	}
	
	@Inject
	private Logger logger;
	
	public Result list(
			@Param(value="page") int page) {
		String method = new Throwable().getStackTrace()[0].getMethodName();
		String link = router.getReverseRoute(getClass(), method);
		
		IQueryable<Account> query = accountDao.all();
		PageList<Account> accounts = query.toPageList(link, page, 10);
		
		return Results.ok()
				.render("accounts", accounts)
				.supportedContentTypes(
					Result.TEXT_HTML,
					Result.APPLICATON_JSON
				);
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
			return Results.json().render(JResponse.success("user/account/list"));
		} else {
			return Results.json().render(JResponse.fail(msg.get("account.usernameNotUnique", language).get()));
		}
	}
	
	
	public Result profile() {
		
		return Results.html();
	}
	public Result add() {
		List<Role> roles = roleDao.all().toList(); 
		return Results.html().render("roles", roles);
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
