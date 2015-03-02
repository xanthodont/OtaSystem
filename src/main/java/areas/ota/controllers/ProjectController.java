package areas.ota.controllers;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import utils.StringUtil;
import areas.ota.models.Project;
import areas.user.models.Account;

import com.google.inject.Inject;

import controllers.BaseController;
import dao.base.IDatabase;
import dao.base.IQueryable;
import filters.AuthorizationFilter;
import models.IPageList;
import models.JResponse;
import models.PageList;
import models.Pager;
import models.SelectOption;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.Router;
import ninja.i18n.Messages;
import ninja.params.Param;
import ninja.params.PathParam;

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
	
	public Result list(
			@Param(value="page") int page, 
			@Param(value="product") String product,
			@Param(value="language") String language) {
		String method = new Throwable().getStackTrace()[0].getMethodName();
		String link = router.getReverseRoute(getClass(), method);
		
		IQueryable<Project> query = projectDao.all();
		if (!StringUtil.isEmpty(product)) query = query.where(c -> c.equals("product", product));
		if (!StringUtil.isEmpty(language)) query = query.where(c -> c.equals("language", language));
		PageList<Project> projects = query.toPageList(link, page, 5);
		return Results.ok()
				.render("projects", projects)
				.supportedContentTypes(
					Result.TEXT_HTML,
					Result.APPLICATON_JSON
				);
	}
	
	public Result getList() {
		List<Project> projects = projectDao.all().toList();
		return Results.ok()
				.render("projects", projects)
				.supportedContentTypes(
					Result.TEXT_HTML,
					Result.APPLICATON_JSON
				);
	}
	
	public Result add() {
		return Results.ok()
				.render("project", new Project())
				.template("/areas/ota/views/ProjectController/model.ftl.html")
				.supportedContentTypes(
					Result.TEXT_HTML,
					Result.APPLICATON_JSON
				);
	}
	
	public Result edit(@Param(value="id") long id) {
		Project p = projectDao.first(c -> c.equals("id", id));
		return Results.ok()
				.render("project", p)
				.template("/areas/ota/views/ProjectController/model.ftl.html")
				.supportedContentTypes(
					Result.TEXT_HTML,
					Result.APPLICATON_JSON
				);
	}
	
	public Result save(Project project) {
		/**
		 * 处理字符
		 */
		project.setOem(project.getOem().replace("_", "$"));
		project.setProduct(project.getProduct().replace("_", "$"));
		project.setLanguage(project.getLanguage().replace("_", "$"));
		project.setOperator(project.getOperator().replace("_", "$"));
		
		List<Project> pl = projectDao.all()
				.and(c -> c.equals("oem", project.getOem()))
				.and(c -> c.equals("product", project.getProduct()))
				.and(c -> c.equals("language", project.getLanguage()))
				.and(c -> c.equals("operator", project.getOperator()))
				.toList();
		if (project.getId() > 0) { // 编辑
			if (pl !=null && pl.size() > 0) { 
				if (pl.get(0).getId() != project.getId()) {
					return Results.json().render(JResponse.fail(msg.get("project.hasexit", language).get()));
				}
			}
			project.setUpdateTime(System.currentTimeMillis());
			projectDao.update(project).commit();
			return Results.json().render(JResponse.success("ota/project"));
		} else { // 新增
			if (pl !=null && pl.size() > 0) {
				return Results.json().render(JResponse.fail(msg.get("project.hasexit", language).get()));
			} else {
				project.setUpdateTime(System.currentTimeMillis());
				projectDao.insert(project).commit();
				return Results.json().render(JResponse.success("ota/project"));
			} 
		}
	} 
	
	public Result delete(@Param(value="id") long id) {
		Project p = projectDao.first(c -> c.equals("id", id));
		if (p == null) {
			return Results.json().render(JResponse.fail(""));
		} else {
			projectDao.delete(p).commit();
			return Results.json().render(JResponse.success("ota/project"));
		}
	}
	
	public Result getProject(@Param(value="id") long id) {
		Project p = projectDao.first(c -> c.equals("id", id));
		if (p == null) {
			return Results.json().render("r", JResponse.fail(""));
		} else {
			return Results.json().render("r", JResponse.success("ota/project")).render("project", p);
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
