package com.taobao.session.config;

import static com.taobao.session.util.ConfigUtils.fetchConfigEntriesFromFactory;
import static com.taobao.session.util.ConfigUtils.getSpringConfigFactoryFromContext;

import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

//import com.taobao.config.client.SubscriberDataObserver;
import com.taobao.session.IllegalConfigException;
//import com.taobao.util.crypter.Md5Encrypter;

public class ConfigServerXmlConfig extends AbstractConfigServerConfig {

    private static final Logger logger = Logger.getLogger(ConfigServerXmlConfig.class);

    public static final String CONFIG_XML_DATA_ID = "com.taobao.session.config.xml";

    private static final String CONFIG_XML_SUBSCRIBER = "ConfigXmlSubscriber";

    private  String  md5Cache = "";
//    @Override
    protected CountDownLatch setDataObservers() {
//        final CountDownLatch configFetched = new CountDownLatch(1);
//        setDataObserver(CONFIG_XML_SUBSCRIBER, CONFIG_XML_DATA_ID, configFetched, new SubscriberDataObserver() {
//            public void handleData(String dataId, List<Object> data) {
//                fetchConfigsFromData(data);
//            }
//        });
//        return configFetched;
    	return null;
    }

//    private void fetchConfigsFromData(List<Object> data) {
//        String xml = findFirstString(data);
//        String tempMd5 = Md5Encrypter.md5(xml);
//		if(tempMd5.equals(md5Cache)){
//           logger.warn("检测到相同配置，忽略");
//           return ;
//        }else{
//           md5Cache =  Md5Encrypter.md5(xml);
//        }
//        ApplicationContext context = new StringXmlApplicationContext(xml);
//        SpringConfigFactory factory = getSpringConfigFactoryFromContext(context);
//        Collection<Properties> properties = factory.getProperties();
//        if (null == properties) {
//            throw new IllegalConfigException("生成XML配置对象失败,session的properties必须配置");
//        }
//        Collection<Properties> configEntries = fetchConfigEntriesFromFactory(factory);
//        fetchDefaultConfig(factory);
//        ConfigEntriesResult configEntriesResult = fetchConfigEntriesFromData(configEntries);
//        PropertiesResult propertiesResult = fetchPropertiesFromData(properties);
//        fetchResults(configEntriesResult, propertiesResult);
//    }

//    private void fetchResults(ConfigEntriesResult configEntriesResult, PropertiesResult propertiesResult) {
//        int latestVersion = Math.max(configEntriesResult.latestVersion, propertiesResult.latestVersion);
//        checkLatestVersion(latestVersion);
//        this.configEntries = setIfLonger(configEntriesResult.configEntries, this.configEntries);
//        this.configGroups = setIfLonger(configEntriesResult.configGroups, this.configGroups);
//        this.inUseStoreKeys = setIfLonger(configEntriesResult.inUseStoreKeys, this.inUseStoreKeys);
//        this.properties = setIfLonger(propertiesResult.properties, this.properties);
//        // latestVersion必须最后设置，以防止同步问题
//        this.latestVersion = setIfBigger(latestVersion, this.latestVersion);
//    }

//    private String findFirstString(List<Object> data) {
//        if (data == null || data.size() == 0) {
//            throw new IllegalConfigException("配置不能为空");
//        }
//        if (data.size() > 1) {
//            logger.warn("配置超过一份，取第一份配置");
//        }
//
//        for (Object object : data) {
//            if (object instanceof String) {
//                return (String) object;
//            }
//        }
//
//        throw new IllegalConfigException("必须包含一份类型为" + String.class.getName() + "的配置");
//    }

}
