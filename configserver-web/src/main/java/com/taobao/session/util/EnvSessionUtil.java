package com.taobao.session.util;
/**   
 * @author xiaoxie   
 * @create time��2010-1-21 ����02:46:09   
 * @description  EnvSessionUtil �����Ի���ʹ��,���Ի���hsfͨ������EnvSessionUtil.getEnvProjectName()����ȡ��ǰ����Ŀ��.
 * ��Ŀ����Ӧ��cookieΪ"wc2010"
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
