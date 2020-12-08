package com.taobao.session.config;

import java.util.List;
import java.util.concurrent.CountDownLatch;

//import com.taobao.config.client.SubscriberDataObserver;

/**
 * @author hengyi
 */
public class ConfigServerPropertiesConfig extends AbstractConfigServerConfig {

    public static final String CONFIG_ENTRIES_DATA_ID = "com.taobao.session.config.entries";

    public static final String CONFIG_PROPERTIES_DATA_ID = "com.taobao.session.config.properties";

    private static final String CONFIG_ENTRIES_SUBSCRIBER = "ConfigEntriesSubscriber";

    private static final String CONFIG_PROPERTIES_SUBSCRIBER = "ConfigPropertiesSubscriber";

    @Override
    protected CountDownLatch setDataObservers() {
        CountDownLatch configFetched = new CountDownLatch(2);
//        setDataObserver(CONFIG_ENTRIES_SUBSCRIBER, CONFIG_ENTRIES_DATA_ID, configFetched, new SubscriberDataObserver() {
//            public void handleData(String dataId, List<Object> data) {
//                ConfigEntriesResult result = fetchConfigEntriesFromData(data);
//                checkLatestVersion(result.latestVersion);
//                configEntries = setIfLonger(result.configEntries, configEntries);
//                configGroups = setIfLonger(result.configGroups, configGroups);
//                inUseStoreKeys = setIfLonger(result.inUseStoreKeys, inUseStoreKeys);
//                // latestVersion必须最后设置，以防止同步问题
//                latestVersion = setIfBigger(result.latestVersion, latestVersion);
//            }
//        });
//        setDataObserver(CONFIG_PROPERTIES_SUBSCRIBER, CONFIG_PROPERTIES_DATA_ID, configFetched,
//                new SubscriberDataObserver() {
//                    public void handleData(String dataId, List<Object> data) {
//                        PropertiesResult result = fetchPropertiesFromData(data);
//                        checkLatestVersion(result.latestVersion);
//
//                        properties = setIfLonger(result.properties, properties);
//                        // latestVersion必须最后设置，以防止同步问题
//                        latestVersion = setIfBigger(result.latestVersion, latestVersion);
//                    }
//                });
        return configFetched;
    }

}
