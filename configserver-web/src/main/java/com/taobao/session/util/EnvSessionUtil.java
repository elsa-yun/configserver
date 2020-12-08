package com.taobao.session.util;
/**   
 * @author xiaoxie   
 * @create time：2010-1-21 下午02:46:09   
 * @description  EnvSessionUtil 供测试环境使用,测试环境hsf通过调用EnvSessionUtil.getEnvProjectName()来获取当前的项目名.
 * 项目名对应的cookie为"wc2010"
 */
public class EnvSessionUtil {
	
	public static String ENV_PROJECT_NAME_COOKIE_KEY = "wc2010";
	
	private static ThreadLocal<String> envPName = new ThreadLocal<String>();
	
	public static  String getEnvProjectName () {
		return envPName.get();
	}
	public static void setEnvProjectName(String projectName) {
		if  (projectName != null)  {
			envPName.set(projectName);
		}
	}
}
