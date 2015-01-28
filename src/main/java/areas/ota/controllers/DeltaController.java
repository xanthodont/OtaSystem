package areas.ota.controllers;

import java.util.List;

import com.google.inject.Inject;

import areas.ota.models.Delta;
import areas.ota.models.Version;
import models.JResponse;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.i18n.Messages;
import ninja.params.Param;
import controllers.BaseController;
import dao.base.IDatabase;
import filters.AuthorizationFilter;

@FilterWith(AuthorizationFilter.class)
public class DeltaController extends BaseController {
	@Inject
	private IDatabase<Delta> dao;

	@Inject
	public DeltaController(Messages msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}
	
	public Result list() {
		List<Delta> entities = dao.all().toList();
		
		return Results.html().render("deltas", entities);
	}
	
	public Result add() {
		
		return Results.html();
	}
	
	public Result save(Delta delta) {
		return Results.ok();
	}
	
	public Result delete(@Param(value="id") long id) {
		Delta entity = dao.first(c -> c.equals("id", id));
		if (entity == null) {
			return Results.json().render(JResponse.fail("fail"));
		} else {
			dao.delete(entity).commit();
			return Results.json().render(JResponse.success(""));
		}
	}
	
	public Result setStatus(@Param(value="id") long id, @Param(value="status") int status) {
		Delta entity = dao.first(c -> c.equals("id", id));
		if (entity == null) {
			return Results.json().render(JResponse.fail("fail"));
		} else {
			entity.setStatus(status);
			dao.update(entity).commit();
			return Results.json().render(JResponse.success(""));
		}
	}

}
