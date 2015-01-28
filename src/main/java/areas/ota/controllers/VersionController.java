package areas.ota.controllers;

import java.util.List;

import areas.ota.models.Delta;
import areas.ota.models.Project;
import areas.ota.models.Version;

import com.google.inject.Inject;

import controllers.BaseController;
import dao.base.IDatabase;
import filters.AuthorizationFilter;
import models.JResponse;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.i18n.Messages;
import ninja.params.Param;

@FilterWith(AuthorizationFilter.class)
public class VersionController extends BaseController {
	@Inject
	private IDatabase<Project> projectDao;
	@Inject
	private IDatabase<Version> dao;
	@Inject 
	private IDatabase<Delta> deltaDao;
	
	@Inject
	public VersionController(Messages msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	public Result index() {
		return Results.html();
	}
	
	public Result list() {
		List<Version> versions = dao.all().toList();
		
		return Results.html().render("versions", versions);
	}
	
	public Result add() {
		
		return Results.html();
	}
	
	
	public Result save(Version version, @Param(value="projectId") long projectId) {
		long count = dao.count()
				.and(c -> c.equals("buildNumber", version.getBuildNumber()))
				.toCount();
		if (count > 0) {
			return Results.json().render(JResponse.fail(msg.get("version.hasexit", language).get()));
		} else {
			Project project = projectDao.first(c -> c.equals("id", projectId));
			if (project == null) {
				return Results.json().render(JResponse.fail(msg.get("version.projectnotexit", language).get()));
			}
			List<Version> fromVersions = dao.all().where(c -> c.equals("projectId", projectId)).toList();
			version.setProject(project);
			version.setCreateTime(System.currentTimeMillis());
			dao.insert(version).commit();
			// add delta
			for (Version fv : fromVersions) {	
				Delta delta = new Delta();
				delta.setFromVersion(fv);
				delta.setToVersion(version);
				delta.setUpdateTime(System.currentTimeMillis());
				deltaDao.insert(delta).commit();
			}
			return Results.json().render(JResponse.success("/ota/version"));
		} 
	} 
	
	public Result delete(@Param(value="id") long id) {
		Version entity = dao.first(c -> c.equals("id", id));
		if (entity == null) {
			return Results.json().render(JResponse.fail("fail"));
		} else {
			dao.delete(entity).commit();
			return Results.json().render(JResponse.success(""));
		}
	}
	
}
