package filters;

import com.google.inject.Inject;

import ninja.Context;
import ninja.Filter;
import ninja.FilterChain;
import ninja.Ninja;
import ninja.Result;
import ninja.Results;
import ninja.UsernamePasswordValidator;

/**
 * 用户登录验证
 * 
 * @author xanthodont
 *
 */
public class AuthorizationFilter implements Filter{
	
	/** If a username is saved we assume the session is valid */
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    
    private final Ninja ninja;
    protected final UsernamePasswordValidator credentialsValidator;
    
    @Inject
    public AuthorizationFilter(Ninja ninja, UsernamePasswordValidator validator) {
		// TODO Auto-generated constructor stub
    	this.ninja = ninja;
    	this.credentialsValidator = validator;
	}

    /**
     * 															  |--No-->  fileterChain.next	// 1-1		
     * AuthorizationFilter ----> (session["username"] == null)? --|
     * 										  					  |--Yes-->  (cookie["username"]!=null && cookie["password"]!=null) ----> // 1-2 
     * 	
     * 			 |--No--> html.template("login.ftl.html")  
     * // 1-2  --|										  |--No--> html.template("login.ftl.html")  
     * 			 |--Yes--> Verify(username, password)   --|
     * 									  				  |--Yes--> session.put("username", username) ----> filterChain.next
     */
	@Override
	public Result filter(FilterChain filterChain, Context context) {
		// TODO Auto-generated method stub
		if (context.getSession() == null 
		 || context.getSession().get(USERNAME) == null) {
			if (context.getCookie(USERNAME) != null && context.getCookie(PASSWORD) != null) {
				String username = context.getCookie(USERNAME).getValue();
				String password = context.getCookie(PASSWORD).getValue();
				if (credentialsValidator.validateCredentials(username, password)) {
					context.getSession().put(USERNAME, username);
					return filterChain.next(context);
				} 
			}
    		return Results.forbidden().html().template("/views/ApplicationController/loginPage.ftl.html");
    	} else {
    		return filterChain.next(context);
    	}
	}

}
