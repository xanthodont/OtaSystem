package areas.ota.controllers;

import java.util.List;

import areas.ota.models.Delta;
import areas.ota.models.Project;
import areas.ota.models.Version;

import com.google.inject.Inject;

import controllers.BaseController;
import dao.base.IDatabase;
import dao.base.IQueryable;
import filters.AuthorizationFilter;
import models.JResponse;
import models.PageList;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.i18n.Messages;
import ninja.params.Param;
import ninja.params.PathParam;

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
	
	public Result list(
			@Param("page") int page,
			@Param("projectId") long projectId) {
		String method = new Throwable().getStackTrace()[0].getMethodName();
		String link = router.getReverseRoute(getClass(), method);
		
		IQueryable<Version> query = dao.all();
		if (projectId > 0) query.and(c -> c.equals("projectId", projectId));
		PageList<Version> versions = query.toPageList(link, page, 10);
		
		return Results.html().render("versions", versions);
	}
	
	public Result getList(@Param("q") String versionName) {
		List<Version> versions = dao.all().where(c -> c.like("versionName", versionName)).toList();
		return Results.ok()
				.render("versions", versions)
				.supportedContentTypes(
					Result.TEXT_HTML,
					Result.APPLICATON_JSON
				);
	}
	
	public Result add() {
		
		return Results.html();
	}
	
	
	public Result save(Version version, @Param(value="projectId") long projectId) {
		/**
		 * 处理字符
		 */
		version.setBuildNumber(version.getBuildNumber().replace("_", "$"));
		
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
			return Results.json().render(JResponse.success("ota/version"));
		} 
	} 
	
	public Result delete(@Param(value="id") long id) {
		Version entity = dao.first(c -> c.equals("id", id));
		if (entity == null) {
			return Results.json().render(JResponse.fail("fail"));
		} else {
			dao.delete(entity).commit();
			return Results.json().render(JResponse.success("ota/version/list"));
		}
	}
	
}
