package areas.ota.controllers;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import areas.ota.models.Project;
import areas.user.models.Account;

import com.google.inject.Inject;

import dao.base.IDatabase;
import dao.base.IQueryable;
import models.JResponse;
import models.SelectOption;
import ninja.Result;
import ninja.Results;
import ninja.params.Param;

public class ProjectController {
	@Inject
	private IDatabase<Project> projectDao;
	
	public Result index() {
		return Results.html();
	}
	
	public Result list() {
		List<Project> projects = projectDao.all().toList();
		
		return Results.html().render("projects", projects);
	}
	
	public Result add() {
		return Results.html();
	}
	
	public Result save(Project project) {
		long count = projectDao.all()
				.where(c -> c.equals("oem", project.getOem()))
				.and(c -> c.equals("product", project.getProduct()))
				.and(c -> c.equals("language", project.getLanguage()))
				.and(c -> c.equals("operator", project.getOperator()))
				.toCount();
		if (count > 0) {
			return Results.json().render(JResponse.fail("has exit"));
		} else {
			projectDao.insert(project).commit();
			return Results.json().render(JResponse.success(""));
		} 
	} 
	
	public Result getProperty(@Param(value="propertyName") String propertyName) {
		List<Project> projects = projectDao.all().toList();
		Function<? super Project, ? extends String> f = Project::getOem; 
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
		List<String> propertyList = projects.stream().map(f).collect(Collectors.toList());
		List<String> properties = propertyList.stream().distinct().collect(Collectors.toList());
		return Results.json().render(properties);
	}

	private Collector toSelectOption() {
		// TODO Auto-generated method stub
		return null;
	}
}
