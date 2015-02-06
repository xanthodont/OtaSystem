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

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import service.AccountServiceImpl;
import service.IAccountService;
import ninja.UsernamePasswordValidator;
import areas.ota.models.Delta;
import areas.ota.models.Project;
import areas.ota.models.TestImei;
import areas.ota.models.Version;
import areas.user.models.Account;
import areas.user.models.Profile;
import areas.user.models.Role;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

import dao.AccountDao;
import dao.DeltaDao;
import dao.HAccountDao;
import dao.IBasicDao;
import dao.ProfileDao;
import dao.ProjectDao;
import dao.RoleDao;
import dao.TestImeiDao;
import dao.VersionDao;
import dao.base.IDatabase;

@Singleton
public class Module extends AbstractModule {
    

    protected void configure() {
    	configDatabases();
    	configDaos();
    	configServices();
    	bindFilters();
    }

	private void configDatabases() {
		// TODO Auto-generated method stub
		bind(SessionFactory.class).toInstance(new Configuration().configure().buildSessionFactory());
	}

	private void configDaos() {
		// TODO Auto-generated method stub
		//bind(TypeLiteral.class).toInstance(arg0);
		bind(new TypeLiteral<IDatabase<Account>>() {})
		.to(new TypeLiteral<AccountDao>(){});
	
		bind(new TypeLiteral<IDatabase<Profile>>() {})
		.to(new TypeLiteral<ProfileDao>(){});
	 
		bind(new TypeLiteral<IDatabase<Role>>() {})
		.to(new TypeLiteral<RoleDao>(){});
		
		bind(new TypeLiteral<IDatabase<Project>>() {})
		.to(new TypeLiteral<ProjectDao>(){});
		
		bind(new TypeLiteral<IDatabase<Version>>() {})
		.to(new TypeLiteral<VersionDao>(){});
		
		bind(new TypeLiteral<IDatabase<Delta>>() {})
		.to(new TypeLiteral<DeltaDao>(){});
		
		bind(new TypeLiteral<IDatabase<TestImei>>() {})
		.to(new TypeLiteral<TestImeiDao>(){});
	}
	
	private void configServices() {
		bind(IAccountService.class).to(AccountServiceImpl.class);
	}

	private void bindFilters() {
		// TODO Auto-generated method stub
		/**  */
		bind(UsernamePasswordValidator.class).to(AccountDao.class);
		//bind();
	}

}
