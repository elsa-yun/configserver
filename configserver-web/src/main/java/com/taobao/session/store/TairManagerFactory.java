package com.taobao.session.store;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.taobao.session.util.ConfigUtils;
//import com.taobao.tair.TairManager;
//import com.taobao.tair.impl.DefaultTairManager;

/**
 * @author hengyi
 */
public class TairManagerFactory {

//    private static Map<String, TairManager> instances = new ConcurrentHashMap<String, TairManager>();

    private static final String CONFIG_SERVER_GROUP_NAME_SEPARATOR = "|";

//    public static TairManager getInstance(String configServers, String groupName) {
//        String key = getKey(configServers, groupName);
//        TairManager instance = instances.get(key);
//        if (instance == null) {
//            synchronized (instances) {
//                instance = instances.get(key);
//                if (instance == null) { // double check
//                    instance = createDefaultInstance(configServers, groupName);
//                    instances.put(key, instance);
//                }
//            }
//        }
//        return instance;
//    }

//    private static DefaultTairManager createDefaultInstance(String configServers, String groupName) {
//        DefaultTairManager defaultInstance = new DefaultTairManager();
//
//        String[] configServerStrings = ConfigUtils.splitConfig(configServers);
//        List<String> configServerList = Arrays.asList(configServerStrings);
//        defaultInstance.setConfigServerList(configServerList);
//        defaultInstance.setGroupName(groupName);
//
//        defaultInstance.init();
//        return defaultInstance;
//    }

    private static String getKey(String configServers, String groupName) {
        return configServers + CONFIG_SERVER_GROUP_NAME_SEPARATOR + groupName;
    }

    // 以下方法只用于单元测试
//    public static void setInstance(String configServers, String groupName, TairManager instance) {
//        String key = getKey(configServers, groupName);
//        if (instance == null) {
//            instances.remove(key);
//        } else {
//            instances.put(key, instance);
//        }
//    }

}
