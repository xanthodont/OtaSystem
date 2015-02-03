package controllers;



import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.MD5;
import utils.StringUtil;
import areas.ota.models.Delta;
import areas.ota.models.Project;
import areas.ota.models.Version;

import com.google.inject.Inject;

import dao.base.IDatabase;
import models.CheckVersionResponse;
import models.LoginResponse;
import models.OtaConstants;
import models.OtaResponse;
import ninja.Context;
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

	private Logger logger = LoggerFactory.getLogger(ApiController.class);
	
	@Inject
	public ApiController(Messages msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	public Result login(
			@Param(value="imei") String imei,
			@Param(value="sn") String sn,
			@Param(value="sim") String sim,
			@Param(value="operator") String operator,
			Session session
			) {
		logger.debug(String.format("Param --- imei:%s, sn:%s, sim:%s, operator:%s", imei, sn, sim, operator));
		boolean istestDevice = false;
		ResourceBundle bundle = ResourceBundle.getBundle("config");
		String otaSN = bundle.getString("sn");
		String mode = bundle.getString("mode");
		/** 验证SN */
		if (sn.equals(otaSN)) {
			// 测试模式
			if (StringUtil.isEmpty(mode) && mode.equals("debug")) {
				istestDevice = true;
			} else { // 判断是否是测试IMEI号
				istestDevice = true;
			}
			
			/** 设置Session值 */
			int rand = new Random().nextInt();
			String token = MD5.md5Digest(sn + String.valueOf(rand)).toLowerCase();
			session.put(OtaConstants.SESSION_ISTEST_DEVICE, String.valueOf(istestDevice));
			session.put(OtaConstants.SESSION_SN, sn);
			session.put(OtaConstants.SESSION_RAND, String.valueOf(rand));
			session.put(OtaConstants.SESSION_TOKEN, token);
			
			return Results.json().render(new LoginResponse(
					OtaConstants.success_code,
					"",
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
			Session session) {
		
		boolean istestDevice = Boolean.parseBoolean(session.get(OtaConstants.SESSION_ISTEST_DEVICE));
		//istestDevice = true;
		logger.debug(String.format("Param --- version:%s, token:%s, istest:%b", projectVersion, token, istestDevice));
		//logger.debug(String.format("Session --- "));
		if (istestDevice || token.equals(session.get(OtaConstants.SESSION_TOKEN))) {
			String[] projects = projectVersion.split("_");
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
					//.stream()
					//.filter(p -> buildNumber.equals(p.getBuildNumber()))
					//.collect(Collectors.toList());
			if (versionList.size() != 1) { // 必须是有且仅有一个
				return Results.json().render(new OtaResponse(OtaConstants.version_invalid_code, OtaConstants.version_invalid_info));
			}
			Version clientVersion = versionList.get(0);
			
			/** 查找客户当前版本可以升级的拆分包 */
			Delta delta = null;
			if (istestDevice) { // 测试情况
				delta = (Delta) deltaDao.first()
						.and(c -> c.equals("fromVersionId", clientVersion.getId()))
						.and(c -> c.equals("status", 2))
						.orderBy(false, "toVersionId")
						.toEntity();
				
			} else { // 实际情况
				delta = (Delta) deltaDao.first()
						.and(c -> c.equals("fromVersionId", clientVersion.getId()))
						.and(c -> c.equals("status", 5)) 
						.orderBy(false, "toVersionId")
						.toEntity();
			}
			if (delta == null) {
				return Results.json().render(new OtaResponse(OtaConstants.version_latest_code, OtaConstants.version_latest_info));
			}
			session.put(OtaConstants.SESSION_VERSION_ID, String.valueOf(delta.getId()));
			session.put(OtaConstants.SESSION_BUILD_NUMBER, delta.getToVersion().getBuildNumber());
			
			CheckVersionResponse cvr = new CheckVersionResponse();
			cvr.setStatus(OtaConstants.success_code);
			cvr.setAndroid_version(delta.getToVersion().getAndroidVersion());
			cvr.setFingerprint(delta.getToVersion().getFingerprint());
			cvr.setName(delta.getToVersion().getBuildNumber());
			cvr.setRelease_notes(delta.getToVersion().getDescription());
			cvr.setSize(delta.getSize());
			cvr.setDeltaId(delta.getId());
			return Results.json().render(cvr);
		}
		return Results.json().render(new OtaResponse(OtaConstants.token_invalid_code, OtaConstants.token_invalid_info));
	}
	
	public Result download(
			@Param(value="token") String token,
			@Param(value="deltaId") long deltaId,
			@Param(value="HTTP_RANGE") int range,
			@Param(value="version") String buildNumber,
			Context context,
			Session session) {
		logger.debug(String.format("Param --- token:%s, deltaId:%d, range:%d, buildNumber:%s", token, deltaId, range, buildNumber));
		return Results.ok();
	}
}
