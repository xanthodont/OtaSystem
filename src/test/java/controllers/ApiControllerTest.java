package controllers;

import org.doctester.testbrowser.Request;
import org.doctester.testbrowser.Response;
import org.junit.Test;
import ninja.NinjaDocTester;

public class ApiControllerTest extends NinjaDocTester{
	private String imei = "314005994012";
	private String sn = "15811375356";// + "123";
	
	private String loginUrl = "/api/login";
	private String checkversionUrl = "/api/checkversion";
	private String downloadUrl = "/api/download";
	
	@Test
	public void loginTest() {
		Response response = makeRequest(Request.POST().url(testServerUrl().path(loginUrl))
				.addFormParameter("imei", imei)
				.addFormParameter("sn", sn)
				.addFormParameter("sim", "10086")
				.addFormParameter("operator", "46000"));
		System.out.printf("Login Result:%s\n", response.payload);
	}
	
	//@Test
	public void checkversionTest() {
		Response response = makeRequest(Request.GET().url(testServerUrl().path(checkversionUrl)).addFormParameter("", "1"));
				
	}
	
	//@Test
	public void downloadTest() {
		Response response = makeRequest(Request.GET().url(testServerUrl().path(downloadUrl)));
	}
	
}
