package areas.ota.controllers;

import java.util.List;

import utils.StringUtil;
import areas.ota.models.TestImei;
import areas.ota.models.Version;

import com.google.inject.Inject;

import models.JResponse;
import models.PageList;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.i18n.Messages;
import ninja.params.Param;
import controllers.BaseController;
import dao.base.IDatabase;
import dao.base.IQueryable;
import filters.AuthorizationFilter;

@FilterWith(AuthorizationFilter.class)
public class TestImeiController extends BaseController {
	@Inject
	private IDatabase<TestImei> dao;
	
	@Inject
	public TestImeiController(Messages msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}
	
	public Result list(
			@Param("page") int page) {
		String method = new Throwable().getStackTrace()[0].getMethodName();
		String link = router.getReverseRoute(getClass(), method);
		
		IQueryable<TestImei> query = dao.all();
		
		PageList<TestImei> imeis = query.toPageList(link, page, 10);
		
		return Results.html().render("imeis", imeis);
	}
	
	public Result getList(@Param("q") String imei) {
		IQueryable<TestImei> query = dao.all();
		if (!StringUtil.isEmpty(imei)) query.where(c -> c.like("imei", imei));
		List<TestImei> imeis = query.toList();
		TestImei q= new TestImei();
		q.setImei(imei);
		imeis.add(q);
		return Results.json().render("imeis", imeis);
	}
	
	public Result add() {
		
		return Results.html();
	}
	
	public Result setStatus(@Param("id") long id, @Param("status") int status) {
		TestImei t = dao.first(c -> c.equals("id", id));
		if (t == null) {
			return Results.json().render(JResponse.fail("fail"));
		} else {
			t.setStatus(status);
			dao.update(t).commit();
			return Results.json().render(JResponse.success("ota/testimei/list"));
		}
		
	}
	
	public Result delete(@Param(value="id") long id) {
		TestImei entity = dao.first(c -> c.equals("id", id));
		if (entity == null) {
			return Results.json().render(JResponse.fail("fail"));
		} else {
			dao.delete(entity).commit();
			return Results.json().render(JResponse.success("ota/testimei/list"));
		}
	}
	
	public Result save(TestImei testImei) {
		long count = dao.count()
				.and(c -> c.equals("imei", testImei.getImei()))
				.toCount();
		if (count > 0) {
			return Results.json().render(JResponse.fail(msg.get("testimei.hasexit", language).get()));
		} else {
			testImei.setStatus(1);
			dao.insert(testImei).commit();
			return Results.json().render(JResponse.success("ota/testImei"));
		} 
	}
}
