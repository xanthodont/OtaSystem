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

import ninja.Result;
import ninja.Results;
import areas.account.models.Account;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import dao.IBasicDao;


@Singleton
public class ApplicationController {
	
	@Inject
	private IBasicDao<Account> dao;

    public Result index() {

        return Results.html();

    }
    
    public Result helloWorldJson() {
    	Account account = new Account();
		account.setUsername("username1");
		account.setPassword("password");
		account.setEmail("abc@konka.com");
		account.setRegisterTime(System.currentTimeMillis());
		//dao.insert(account).commit();
		
    	List<Account> accountList = dao.all();
        SimplePojo simplePojo = new SimplePojo();
        simplePojo.content = String.format("size:%d, username: ", accountList.size());

        return Results.json().render(simplePojo);

    }
    
    public Result test() {
    	return Results.html();
    }
    
    public static class SimplePojo {

        public String content;
        
    }
}
