package areas.ota.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			@Param("page") int page,
			@Param("imei") String imei) {
		String method = new Throwable().getStackTrace()[0].getMethodName();
		String link = router.getReverseRoute(getClass(), method);
		
		IQueryable<TestImei> query = dao.all();
		if (!StringUtil.isEmpty(imei) && !imei.equals("-1")) query.where(c -> c.like("imei", imei));
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
	
	public Result save(@Param("imeis") String imeis) {
		List<String> list = new ArrayList<String>(); 
		String[] imeiList = imeis.split("\n");
		for (int i = 0, len = imeiList.length; i < len; i++) {
			String imei = imeiList[i];
			long count = dao.count()
					.and(c -> c.equals("imei", imei))
					.toCount();
			if (count > 0) {
				list.add(imei);
			} else {
				TestImei ti = new TestImei();
				ti.setStatus(1);
				ti.setImei(imei);
				dao.insert(ti).commit();
			}
		}
		if (list.size() > 0) {
			return Results.json().render("r", JResponse.fail(msg.get("testimei.hasexit", language).get()))
								 .render("l", list);
		} else {
			return Results.json().render("r", JResponse.success("ota/testimei"));
		}
	}
}
