package areas.user.controllers;

import java.util.List;

import areas.ota.models.Delta;
import areas.user.models.Role;

import com.google.inject.Inject;

import ninja.Result;
import ninja.Results;
import ninja.i18n.Messages;
import controllers.BaseController;
import dao.base.IDatabase;

public class RoleController extends BaseController {
	@Inject
	private IDatabase<Role> dao;

	
	@Inject
	public RoleController(Messages msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}
	
	public Result list() {
		List<Role> roles = dao.all().toList();
		return Results.html().render("roles", roles); 
	}
}
