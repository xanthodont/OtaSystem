package areas.ota.controllers;

import java.util.List;

import areas.ota.models.TestImei;
import areas.ota.models.Version;

import com.google.inject.Inject;

import models.JResponse;
import ninja.Result;
import ninja.Results;
import ninja.i18n.Messages;
import controllers.BaseController;
import dao.base.IDatabase;

public class TestImeiController extends BaseController {
	@Inject
	private IDatabase<TestImei> dao;
	
	public TestImeiController(Messages msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}
	
	public Result list() {
		List<TestImei> imeis = dao.all().toList();
		
		return Results.html().render("imeis", imeis);
	}
	
	public Result add() {
		
		return Results.html();
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
