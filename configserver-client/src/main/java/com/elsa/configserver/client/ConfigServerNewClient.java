package com.elsa.configserver.client;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.PriorityOrdered;

import com.elsa.configserver.util.FileUtil;
import com.elsa.configserver.util.HttpClientUtil;

public class ConfigServerNewClient implements BeanFactoryPostProcessor, PriorityOrdered {

	private String appConfigPath;

	private String backConfigPath;

	private String appName;

	private List<String> serverHosts;

	private static Logger log = Logger.getLogger(ConfigServerNewClient.class);

	public ConfigServerNewClient() {

	}

	public void init() {
		HttpClientUtil client = new HttpClientUtil();
		if (null != serverHosts && !serverHosts.isEmpty()) {
			client.setServerHosts(serverHosts);
		}
		Map<String, String> map = client.getConfigContent(getAppName());
		String back_folder_path = get_back_folder() + getAppName() + "/";
		if (!map.isEmpty()) {
			log.info("==============map.isNotEmpty()==========================================" + getAppClassesPath());
			for (Map.Entry<String, String> entry : map.entrySet()) {
				if (null != entry.getKey() && !"".equals(entry.getKey()) && null != entry.getValue() && !"".equals(entry.getValue())) {
					String entry_key = entry.getKey();
					String generate_file_path = getAppClassesPath() + entry_key;
					File file = new File(generate_file_path);
					if (file.exists()) {
						file.delete();
					}
					FileUtil.writeFile(generate_file_path, entry.getValue());
					log.info("generate_file_path=====================>" + generate_file_path + "::::value=>" + entry.getValue());

					String back_file_name = FileUtil.genrateBackFileName(entry_key);

					String source_file_path = generate_file_path;
					String local_folder = back_folder_path;
					String full_target_file_path = local_folder + back_file_name;

					FileUtil.copyFile(source_file_path, full_target_file_path);

					String one_back_folder = FileUtil.getFolder(full_target_file_path);
					log.info("one_back_folder===================>" + one_back_folder);
					FileUtil.setBackFileList(one_back_folder);
				}
			}
		} else {
			log.info("==============  map.isEmpty() ==========================================" + getAppClassesPath());
			Map<String, String> local_file_map = FileUtil.getLocalAllConfigFileMap(back_folder_path);
			if (!local_file_map.isEmpty()) {
				Set<Entry<String, String>> entrySet = local_file_map.entrySet();
				for (Map.Entry<String, String> entry : entrySet) {
					String source_file_path = entry.getValue();
					String ret_file_path = getAppClassesPath() + entry.getKey();
					FileUtil.copyFile(source_file_path, ret_file_path);
					log.info("source_file_path==============>" + source_file_path
							+ ";ret_file_path=============================================>" + ret_file_path);
				}
			}
		}
	}

	public String getAppConfigPath() {
		return appConfigPath;
	}

	public void setAppConfigPath(String appConfigPath) {
		this.appConfigPath = ConfigServerNewClient.class.getResource("/").getPath();
		log.info("==============this.appConfigPath==========================================>" + this.appConfigPath);
	}

	public String getBackConfigPath() {
		return backConfigPath;
	}

	public void setBackConfigPath(String backConfigPath) {
		this.backConfigPath = backConfigPath;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public List<String> getServerHosts() {
		return serverHosts;
	}

	public void setServerHosts(List<String> serverHosts) {
		this.serverHosts = serverHosts;
	}

	private String getAppClassesPath() {
		String osName = System.getProperty("os.name");
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		if (osName.toLowerCase().startsWith("windows")) {
			path = path.substring(1);
		}
		return path;
	}

	private String get_back_folder() {
		String path = getAppClassesPath();
		String[] arrPath = path.split("/");
		int index = 0;
		for (String str : arrPath) {
			if (str.startsWith("webapps")) {
				index = path.indexOf(str);
			}
		}
		return path.substring(0, index) + "backfile/";
	}
	
//	public static void main(String args[]){
//		String path = "E:/app/tomcat7/webapps/show/WEB-INF/classes/";
//		String[] arrPath = path.split("/");
//		int index = 0;
//		for (String str : arrPath) {
//			if (str.startsWith("webapps")) {
//				index = path.indexOf(str);
//			}
//		}
//		String string = path.substring(0, index) + "backfile/";
//		System.out.println(string); 
//	}

	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		
	}

}
