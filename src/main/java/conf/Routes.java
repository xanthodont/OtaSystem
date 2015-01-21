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



import ninja.AssetsController;
import ninja.Router;
import ninja.application.ApplicationRoutes;
import areas.account.controllers.AccountController;
import controllers.ApplicationController;
import controllers.AssetsExController;

public class Routes implements ApplicationRoutes {

    @Override
    public void init(Router router) {  
        
        router.GET().route("/").with(ApplicationController.class, "index");
        router.GET().route("/test").with(ApplicationController.class, "test");
        router.GET().route("/login").with(ApplicationController.class, "login");
        router.GET().route("/admin").with(ApplicationController.class, "admin");
        
 
        ///////////////////////////////////////////////////////////////////////
        // AssetsEx (pictures / javascript)  
        // ��չԭ�е�AssetsController���������֧�����û��棬ʵ��ǰ�������Ż�
        ///////////////////////////////////////////////////////////////////////    
        router.GET().route("/assets/webjars/{fileName: .*}").with(AssetsExController.class, "serveWebJars");
        router.GET().route("/assets/{fileName: .*}").with(AssetsExController.class, "serveStatic");
        
        ///////////////////////////////////////////////////////////////////////
        // Index / Catchall shows index page
        ///////////////////////////////////////////////////////////////////////
        //router.GET().route("/.*").with(ApplicationController.class, "index"); 
        
		///////////////////////////////////////////////////////////////////////
		// Area Extension by xanthodont 
		///////////////////////////////////////////////////////////////////////
        router.GET().route("/account/profile/{accountId}").with(AccountController.class, "profile");
        router.GET().route("/account/login").with(AccountController.class, "login");
        router.GET().route("/account/register").with(AccountController.class, "register");
        router.GET().route("/account/list").with(AccountController.class, "list");
        router.GET().route("/account/add").with(AccountController.class, "add");
    }

}
