package controllers;

import org.slf4j.Logger;

import com.google.inject.Inject;

import ninja.AssetsController;
import ninja.Context;
import ninja.Result;
import ninja.utils.DateUtil;
import ninja.utils.HttpCacheToolkit;
import ninja.utils.MimeTypes;
import ninja.utils.NinjaProperties;

public class AssetsExController extends AssetsController{
	@Inject
	private Logger logger;
	
	@Inject
	public AssetsExController(HttpCacheToolkit httpCacheToolkit,
			MimeTypes mimeTypes, NinjaProperties ninjaProperties) {
		super(httpCacheToolkit, mimeTypes, ninjaProperties);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Result serveStatic(Context context) {
		return super.serveStatic(context)
				.addHeader(Result.CACHE_CONTROL, "max-age=90")
				.addHeader(Result.DATE, DateUtil.formatForHttpHeader(System.currentTimeMillis()+90L))
				.addHeader(Result.EXPIRES, DateUtil.formatForHttpHeader(90L));
	}

	
}
