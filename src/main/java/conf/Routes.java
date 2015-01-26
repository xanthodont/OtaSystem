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




import ninja.Router;
import ninja.application.ApplicationRoutes;
import areas.user.controllers.AccountController;
import areas.ota.controllers.ProjectController;
import areas.ota.controllers.VersionController;
import controllers.ApplicationController;
import controllers.AssetsExController;

public class Routes implements ApplicationRoutes {

    @Override
    public void init(Router router) {  
        
        router.GET().route("/").with(ApplicationController.class, "admin");
        router.GET().route("/test").with(ApplicationController.class, "test");
        router.GET().route("/login").with(ApplicationController.class, "login");
        router.GET().route("/admin").with(ApplicationController.class, "admin");
        
 
        ///////////////////////////////////////////////////////////////////////
        // AssetsEx (pictures / javascript)  
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
        router.GET().route("/user/account").with(AccountController.class, "list");
        //router.GET().route("/user/privilege").with(AccountController.class, "index");

        router.GET().route("/user/account/profile/{accountId}").with(AccountController.class, "profile");
        router.POST().route("/user/account/login").with(AccountController.class, "login");
        router.GET().route("/user/account/logout").with(AccountController.class, "logout");
        router.GET().route("/user/account/register").with(AccountController.class, "register");
        router.GET().route("/user/account/list").with(AccountController.class, "list");
        router.GET().route("/user/account/edit").with(AccountController.class, "edit");
        router.GET().route("/user/account/add").with(AccountController.class, "add");
        
        router.GET().route("/ota/project").with(ProjectController.class, "index");
        router.GET().route("/ota/version").with(VersionController.class, "index");
    }

}
