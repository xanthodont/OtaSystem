package controllers;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.MD5;
import utils.ProjectUtil;
import utils.StringUtil;
import areas.ota.models.Delta;
import areas.ota.models.Project;
import areas.ota.models.TestImei;
import areas.ota.models.Version;

import com.google.inject.Inject;

import dao.base.IDatabase;
import models.CheckVersionResponse;
import models.LoginResponse;
import models.OtaConstants;
import models.OtaResponse;
import ninja.Context;
import ninja.Cookie;
import ninja.Result;
import ninja.Results;
import ninja.i18n.Messages;
import ninja.params.Param;
import ninja.session.Session;

public class ApiController extends BaseController {
	@Inject
	private IDatabase<Project> projectDao;
	@Inject
	private IDatabase<Version> versionDao;
	@Inject
	private IDatabase<Delta> deltaDao;
	@Inject
	private IDatabase<TestImei> testImeiDao;

	private Logger logger = LoggerFactory.getLogger(ApiController.class);
	
	@Inject
	private HttpServletRequest  httpServletRequest;
	@Inject
	private HttpServletResponse httpServletResponse;
	
	@Inject
	public ApiController(Messages msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	public Result login(
			@Param(value="imei") String imei,
			@Param(value="sn") String sn,
			@Param(value="sim") String sim,
			@Param(value="operator") String operator
			) {
		HttpSession session = httpServletRequest.getSession();
		logger.debug(String.format("SessionId --- %s", session.getId()));
		logger.debug(String.format("Param --- imei:%s, sn:%s, sim:%s, operator:%s", imei, sn, sim, operator));
		boolean istestDevice = false;
		ResourceBundle bundle = ResourceBundle.getBundle("config");
		String otaSN = bundle.getString("sn");
		String mode = bundle.getString("mode");
		String serverVersion = bundle.getString("serverVersion");
		/** 验证SN */
		if (sn.equals(otaSN)) {
			// 测试模式
			if (!StringUtil.isEmpty(mode) && mode.equals("debug")) {
				istestDevice = true;
			} else { // 判断是否是测试IMEI号
				long count = testImeiDao.count()
										.where(c -> c.equals("status", 1))
										.and(c -> c.equals("imei", imei))
										.toCount();
				if (count > 0) 
					istestDevice = true;
			}
			
			/** 设置Session值 */
			int rand = new Random().nextInt();
			String token = MD5.md5Digest(sn + String.valueOf(rand)).toLowerCase();
			session.setAttribute(OtaConstants.SESSION_ISTEST_DEVICE, istestDevice);
			session.setAttribute(OtaConstants.SESSION_SN, sn);
			session.setAttribute(OtaConstants.SESSION_RAND, String.valueOf(rand));
			session.setAttribute(OtaConstants.SESSION_TOKEN, token);
			
			return Results.json().render(new LoginResponse(
					OtaConstants.success_code,
					serverVersion,
					String.valueOf(rand),
					session.getId()
					));
		} else {
			return Results.json().render(new OtaResponse(OtaConstants.sn_error_code, OtaConstants.sn_error_info));
		}
	}
	
	public Result checkVersion(
			@Param(value="version") String projectVersion, 
			@Param(value="token") String token,
			Context context) {
		HttpSession session = httpServletRequest.getSession();
		logger.debug(String.format("SessionId --- %s token:%s", session.getId(), session.getAttribute(OtaConstants.SESSION_TOKEN)));
		boolean istestDevice = (boolean) (session.getAttribute(OtaConstants.SESSION_ISTEST_DEVICE));
		//istestDevice = true;
		logger.debug(String.format("Param --- version:%s, token:%s, istest:%b", projectVersion, token, istestDevice));
		
		if (istestDevice || token.equals(session.getAttribute(OtaConstants.SESSION_TOKEN))) {
			String[] projects = projectVersion.split(";");
			if (projects.length < 4) {
				return Results.json().render(new OtaResponse(OtaConstants.param_lost_code, OtaConstants.param_lost_info));
			}
			String oem = projects[0];
			String product = projects[1];
			String language = projects[2];
			String buildNumber = projects[3];
			//String operator = projects[4];
			//boolean istestDevice = Boolean.parseBoolean(session.get(OtaConstants.SESSION_ISTEST_DEVICE));
			/** 检测项目是否存在 */
			Project project = (Project) projectDao.first()
					.and(c -> c.equals("oem", oem))
					.and(c -> c.equals("product", product))
					.and(c -> c.equals("language", language))
					.toEntity();
			if (project == null) {
				return Results.json().render(new OtaResponse(OtaConstants.version_invalid_code, OtaConstants.version_invalid_info));
			}
			/** 检测该项目的版本号是否存在 */
			List<Version> versionList = versionDao.all()
					.where(c -> c.equals("projectId", project.getId()))
					.and(c -> c.equals("buildNumber", buildNumber))
					.toList();
			if (versionList.size() != 1) { // 必须是有且仅有一个
				return Results.json().render(new OtaResponse(OtaConstants.version_invalid_code, OtaConstants.version_invalid_info));
			}
			Version clientVersion = versionList.get(0);
			
			/** 查找客户当前版本可以升级的拆分包 */
			Delta delta = null;
			if (istestDevice) { // 测试模式
				delta = (Delta) deltaDao.first()
						.and(c -> c.equals("fromVersionId", clientVersion.getId()))
						.and(c -> c.equals("status", 2))
						.orderBy(false, "toVersionId")
						.toEntity();
			} else { // 生产模式
				delta = (Delta) deltaDao.first()
						.and(c -> c.equals("fromVersionId", clientVersion.getId()))
						.and(c -> c.equals("status", 5)) 
						.orderBy(false, "toVersionId")
						.toEntity();
			}
			if (delta == null) {
				logger.info(OtaConstants.version_latest_info);
				return Results.json().render(new OtaResponse(OtaConstants.version_latest_code, OtaConstants.version_latest_info));
			}
			session.setAttribute(OtaConstants.SESSION_VERSION_ID, String.valueOf(delta.getId()));
			session.setAttribute(OtaConstants.SESSION_BUILD_NUMBER, delta.getToVersion().getBuildNumber());
			
			CheckVersionResponse cvr = new CheckVersionResponse();
			cvr.setStatus(OtaConstants.success_code);
			cvr.setAndroid_version(delta.getToVersion().getAndroidVersion());
			//cvr.setFingerprint(delta.getToVersion().getFingerprint());
			cvr.setName(delta.getToVersion().getVersionName());
			cvr.setRelease_notes(delta.getToVersion().getDescription());
			cvr.setSize(delta.getSize());
			cvr.setVersionId(delta.getId());
			logger.info(OtaConstants.success_info);
			return Results.json().render(cvr);
		}
		return Results.json().render(new OtaResponse(OtaConstants.token_invalid_code, OtaConstants.token_invalid_info));
	}
	
	public Result download(
			@Param(value="token") String token,
			@Param(value="versionId") long deltaId,		// 注：客户端原因，不用纠结于此
			@Param(value="HTTP_RANGE") int range,
			@Param(value="version") String buildNumber) {
		logger.debug(String.format("Param --- token:%s, deltaId:%d, range:%d, buildNumber:%s", token, deltaId, range, buildNumber));
		HttpSession session = httpServletRequest.getSession();
		boolean istestDevice = (boolean) (session.getAttribute(OtaConstants.SESSION_ISTEST_DEVICE));
		if (istestDevice || token.equals(session.getAttribute(OtaConstants.SESSION_TOKEN))) {
			Delta delta = deltaDao.first(c -> c.equals("id", deltaId));
			if (delta == null) {
				return Results.notFound();
			}
			String basePath = ProjectUtil.getDeltaSavePath() + String.valueOf(delta.getId());
			File outputFile = new File(basePath+File.separator+delta.getFilePath());
			if (!outputFile.exists()) {
				return Results.notFound();
			}
			
			RandomAccessFile in;
			try {
				in = new RandomAccessFile(outputFile, "r");
				long fileSize = outputFile.length();
				int blockSize = 1024;
				if (range > 0 && range < fileSize) {
					in.seek(range);
					httpServletResponse.setHeader("Content-Length", String.valueOf(fileSize-range));
					httpServletResponse.setHeader("Accept-Ranges", String.format("bytes%d-%d/$d", range, fileSize-1, fileSize));
				} else {
					httpServletResponse.setHeader("Content-Length", String.valueOf(fileSize));
					httpServletResponse.setHeader("Accept-Ranges", "bytes");
				}
				httpServletResponse.setHeader("Cache-Control", "public");
				httpServletResponse.setHeader("Pragma", "public");
				httpServletResponse.setHeader("Content-Disposition", "inline; filename="+outputFile.getName());
				
				
				byte[] bytes = new byte[1024];
				int length = 0;
				ServletOutputStream out = httpServletResponse.getOutputStream();
				while ((length = in.read(bytes)) != -1) {
					out.write(bytes);
				}
				out.flush();
				out.close();
				in.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				logger.info(e.getMessage());
				return Results.notFound();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.info(e.getMessage());
				return Results.status(500);
			}
			return Results.ok();
		} else {
			return Results.json().render(new OtaResponse(OtaConstants.token_invalid_code, OtaConstants.token_invalid_info));
		}
		
	}
}
