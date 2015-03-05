/**
 * Copyright (C) 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import service.IAccountService;
import models.JResponse;
import ninja.BasicAuthFilter;
import ninja.Context;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.Router;
import ninja.SecureFilter;
import ninja.i18n.Messages;
import ninja.params.Param;
import ninja.session.Session;
import areas.user.models.Account;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import dao.IBasicDao;
import filters.AuthorizationFilter;
import filters.PrivilegeFilter;


@Singleton
public class ApplicationController {
	@Inject
	private IAccountService accountService;
	private Messages msg;
	private Optional<String> language;
	private Optional<Result> optResult;
	//@Inject
	//private IBasicDao<Account> dao;
	@Inject
	private Router router;
	private Logger logger = LoggerFactory.getLogger(ApplicationController.class);
	
	@Inject
	public ApplicationController(Messages msg) {
		this.msg = msg;

		language = Optional.of("zh-CN");
        optResult = Optional.absent();
	}
	
	public Result login(
			@Param("username") String username, 
			@Param("password") String password,
			Session session) {
		logger.debug(String.format("SessionId --- %s", session.getId()));
		if (accountService.validateCredentials(username, password, session)) {
			
			return Results.json().render(JResponse.success("admin"));
		} else {
			return Results.json().render(JResponse.fail(msg.get("login.notice.fail", language).get()));
		}
	} 
	
	public Result logout(Session session) {
		session.remove(AuthorizationFilter.USERNAME);
		return Results.redirect(router.getReverseRoute(ApplicationController.class, "loginPage"));
	}
	
	public Result loginPage() {
		return Results.html();
	}

    public Result index(Account account) {
        return Results.html();
    }
    
    @FilterWith({AuthorizationFilter.class})
    public Result admin(Context context, Session session) {
    	//logger.debug(String.format("Param --- version:%s, token:%s, istest:%b", projectVersion, token, istestDevice));
    	logger.debug(String.format("SessionId --- %s", session.getId()));
		logger.debug(String.format("Cookie: %s", context.getCookie("NINJA_SESSION").getValue()));
    	return Results.html();
    }
    
    public Result test() {
    	return Results.html();
    }
    
    public static class SimplePojo {

        public String content;
        
    }
}
