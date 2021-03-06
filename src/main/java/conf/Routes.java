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
import areas.user.controllers.RoleController;
import areas.ota.controllers.DeltaController;
import areas.ota.controllers.ProjectController;
import areas.ota.controllers.TestImeiController;
import areas.ota.controllers.VersionController;
import controllers.ApiController;
import controllers.ApplicationController;
import controllers.AssetsExController;

public class Routes implements ApplicationRoutes {

    @Override
    public void init(Router router) {  
        
        router.GET().route("/").with(ApplicationController.class, "admin");
        router.GET().route("/test").with(ApplicationController.class, "test");
        router.GET().route("/login").with(ApplicationController.class, "loginPage");
        router.GET().route("/logout").with(ApplicationController.class, "logout");
        router.POST().route("/user/account/login").with(ApplicationController.class, "login");
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
        router.POST().route("/user/account/delete").with(AccountController.class, "delete");
        router.GET().route("/user/account/list").with(AccountController.class, "list");
        router.GET().route("/user/account/edit").with(AccountController.class, "edit");
        router.GET().route("/user/account/add").with(AccountController.class, "add");
        router.POST().route("/user/account/save").with(AccountController.class, "save");
        router.GET().route("/user/role").with(RoleController.class, "list");
        router.GET().route("/user/role/list").with(RoleController.class, "list");
        
       
        router.GET().route("/ota/project/list").with(ProjectController.class, "list");
        router.POST().route("/ota/project/list").with(ProjectController.class, "list");
        router.GET().route("/ota/project").with(ProjectController.class, "list");
        router.GET().route("/ota/project/getList").with(ProjectController.class, "getList");
        router.GET().route("/ota/project/getProject").with(ProjectController.class, "getProject");
        router.GET().route("/ota/project/add").with(ProjectController.class, "add");
        router.GET().route("/ota/project/edit").with(ProjectController.class, "edit");
        router.GET().route("/ota/project/getProperty").with(ProjectController.class, "getProperty");
        router.POST().route("/ota/project/save").with(ProjectController.class, "save");
        router.POST().route("/ota/project/delete").with(ProjectController.class, "delete");
        
        
        router.GET().route("/ota/version").with(VersionController.class, "list");
        router.GET().route("/ota/version/list").with(VersionController.class, "list");
        router.POST().route("/ota/version/list").with(VersionController.class, "list");
        router.GET().route("/ota/version/getList").with(VersionController.class, "getList");
        router.GET().route("/ota/version/add").with(VersionController.class, "add");
        router.GET().route("/ota/version/getVersion").with(VersionController.class, "getVersion");
        router.POST().route("/ota/version/save").with(VersionController.class, "save");
        router.POST().route("/ota/version/update").with(VersionController.class, "update");
        router.POST().route("/ota/version/delete").with(VersionController.class, "delete");
        
        
        router.GET().route("/ota/delta/list").with(DeltaController.class, "list");
        router.POST().route("/ota/delta/list").with(DeltaController.class, "list");
        router.GET().route("/ota/delta").with(DeltaController.class, "list");
        
        router.POST().route("/ota/delta/setStatus").with(DeltaController.class, "setStatus");
        router.POST().route("/ota/delta/uploadfile/{deltaId}").with(DeltaController.class, "uploadfile");
        
        router.GET().route("/ota/testimei/list").with(TestImeiController.class, "list");
        router.POST().route("/ota/testimei/list").with(TestImeiController.class, "list");
        router.GET().route("/ota/testimei").with(TestImeiController.class, "list");
        router.GET().route("/ota/testimei/getList").with(TestImeiController.class, "getList");
        router.POST().route("/ota/testimei/setStatus").with(TestImeiController.class, "setStatus");
        router.POST().route("/ota/testimei/delete").with(TestImeiController.class, "delete");
        router.POST().route("/ota/testimei/save").with(TestImeiController.class, "save");
        
        /** API */
        router.POST().route("/api/login").with(ApiController.class, "login");
        router.POST().route("/api/checkversion").with(ApiController.class, "checkVersion");
        router.POST().route("/api/download").with(ApiController.class, "download"); 
        /** 旧版API */
        router.POST().route("/download/login.php").with(ApiController.class, "login");
        router.POST().route("/download/checkversion.php").with(ApiController.class, "checkVersion");
        router.POST().route("/download/downloadfullota.php").with(ApiController.class, "download");
        router.POST().route("/download/download.php").with(ApiController.class, "download"); 
    }

}
