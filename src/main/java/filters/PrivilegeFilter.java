package filters;

import ninja.Context;
import ninja.Filter;
import ninja.FilterChain;
import ninja.Result;

/**
 * 权限过滤器
 * @author xanthodont
 *
 */
public class PrivilegeFilter implements Filter {
	public static final String PRIVILEGE = "privilege";

	@Override
	public Result filter(FilterChain filterChain, Context context) {
		// TODO Auto-generated method stub
		return filterChain.next(context);
	}

}
