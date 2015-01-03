/**
 * Copyright (C) 2012 the original author or authors.
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

package conf;

import areas.account.models.Account;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

import dao.IBasicDao;
import dao.JPABasicDao;

@Singleton
public class Module extends AbstractModule {
    

    protected void configure() {
        //IBasicDao<Account> dao = new JPABasicDao<Account>();
        // bind your injections here!
    	bind(new TypeLiteral<IBasicDao<Account>>() {})
    		//.annotatedWith(Named.names("Language"))
    		.to(new TypeLiteral<JPABasicDao<Account>>(){});
        //bind(new TypeLiteral<IBasicDao<Account>>() {});
    }

}
