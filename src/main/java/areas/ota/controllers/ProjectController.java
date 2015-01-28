package areas.ota.controllers;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import areas.ota.models.Project;
import areas.user.models.Account;

import com.google.inject.Inject;

import controllers.BaseController;
import dao.base.IDatabase;
import dao.base.IQueryable;
import filters.AuthorizationFilter;
import models.JResponse;
import models.SelectOption;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.i18n.Messages;
import ninja.params.Param;

@FilterWith(AuthorizationFilter.class)
public class ProjectController extends BaseController{
	@Inject
	public ProjectController(Messages msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	@Inject
	private IDatabase<Project> projectDao;
	
	public Result index() {
		return Results.ok();
	}
	
	public Result list() {
		List<Project> projects = projectDao.all().toList();
		
		return Results.ok()
				.render("projects", projects)
				.supportedContentTypes(
					Result.TEXT_HTML,
					Result.APPLICATON_JSON
				);
	}
	
	public Result add() {
		return Results.html();
	}
	
	public Result save(Project project) {
		long count = projectDao.count()
				.and(c -> c.equals("oem", project.getOem()))
				.and(c -> c.equals("product", project.getProduct()))
				.and(c -> c.equals("language", project.getLanguage()))
				.and(c -> c.equals("operator", project.getOperator()))
				.toCount();
		if (count > 0) {
			return Results.json().render(JResponse.fail(msg.get("project.hasexit", language).get()));
		} else {
			project.setUpdateTime(System.currentTimeMillis());
			projectDao.insert(project).commit();
			return Results.json().render(JResponse.success("/ota/project"));
		} 
	} 
	
	public Result delete(@Param(value="id") long id) {
		Project p = projectDao.first(c -> c.equals("id", id));
		if (p == null) {
			return Results.json().render(JResponse.fail(""));
		} else {
			projectDao.delete(p).commit();
			return Results.json().render(JResponse.success(""));
		}
	}
	
	public Result getProperty(@Param(value="propertyName") String propertyName) {
		List<Project> projects = projectDao.all().toList();
		Function<? super Project, ? extends String> f = null; 
		switch (propertyName) {
		case "oem":
			f = Project::getOem;
			break;
		case "product":
			f = Project::getProduct;
			break;
		case "language":
			f = Project::getLanguage;
			break;   
		case "operator":
			f = Project::getOperator;
			break;
		}
		if (f == null) {
			return Results.json().render(projects);
		}
		List<String> propertyList = projects.stream().map(f).collect(Collectors.toList());
		List<String> properties = propertyList.stream().distinct().collect(Collectors.toList());
		return Results.json().render(properties);
	}

}
