package controllers;

import ninja.Result;
import ninja.i18n.Messages;

import com.google.common.base.Optional;
import com.google.inject.Inject;

public class BaseController {
	protected Messages msg;
	protected Optional<String> language;
	protected Optional<Result> optResult;
	
	@Inject
	public BaseController(Messages msg) {
		this.msg = msg;

		language = Optional.of("zh-CN");
        optResult = Optional.absent();
	}
}
