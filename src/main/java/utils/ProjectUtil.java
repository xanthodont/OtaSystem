package utils;

import java.io.File;

public class ProjectUtil {
	/**
	 * 获取工程路径
	 * @return
	 */
	public static String getProjectPath() {
		return System.getProperty("user.dir");
	}
	
	public static String getDeltaSavePath() {
		return getProjectPath() + File.separator + "upload" + File.separator;
	}
}
