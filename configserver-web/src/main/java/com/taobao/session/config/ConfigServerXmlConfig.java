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
//           logger.warn("��⵽��ͬ���ã�����");
//           return ;
//        }else{
//           md5Cache =  Md5Encrypter.md5(xml);
//        }
//        ApplicationContext context = new StringXmlApplicationContext(xml);
//        SpringConfigFactory factory = getSpringConfigFactoryFromContext(context);
//        Collection<Properties> properties = factory.getProperties();
//        if (null == properties) {
//            throw new IllegalConfigException("����XML���ö���ʧ��,session��properties��������");
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
//        // latestVersion����������ã��Է�ֹͬ������
//        this.latestVersion = setIfBigger(latestVersion, this.latestVersion);
//    }

//    private String findFirstString(List<Object> data) {
//        if (data == null || data.size() == 0) {
//            throw new IllegalConfigException("���ò���Ϊ��");
//        }
//        if (data.size() > 1) {
//            logger.warn("���ó���һ�ݣ�ȡ��һ������");
//        }
//
//        for (Object object : data) {
//            if (object instanceof String) {
//                return (String) object;
//            }
//        }
//
//        throw new IllegalConfigException("�������һ������Ϊ" + String.class.getName() + "������");
//    }

}
