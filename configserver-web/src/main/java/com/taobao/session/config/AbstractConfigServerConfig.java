package com.taobao.session.config;

import static com.taobao.session.util.ConfigUtils.getPropertiesVersion;
import static com.taobao.session.util.ConfigUtils.populateConfigEntry;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.servlet.FilterConfig;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

//import com.taobao.config.client.Subscriber;
//import com.taobao.config.client.SubscriberDataObserver;
//import com.taobao.config.client.SubscriberRegistrar;
//import com.taobao.config.client.SubscriberRegistration;
import com.taobao.session.ConfigEntry;
import com.taobao.session.IllegalConfigException;
import com.taobao.session.SessionConfig;
import com.taobao.session.util.ConcurrentHashSet;
import com.taobao.session.util.ConfigUtils;

public abstract class AbstractConfigServerConfig implements SessionConfig {

    private static final Logger logger = Logger.getLogger(AbstractConfigServerConfig.class);

    private static  int TIMEOUT_SECONDS = 30;

    public static final String GROUP_PARAM_NAME = "tbsessionConfigGroup";

    public static final String CLIENT_WAIT_TIME = "clientWaitTime";

    private String configServerGroup;

    /**
     * Ϊ�˱�֤�ڶ��߳��µĿɼ��ԣ������ֶζ�������Ϊvolatile�������м������Ͷ�ʹ��concurrent���
     */
    protected volatile Map<String, ConfigEntry>[] configEntries;

    protected volatile Map<String, Collection<ConfigEntry>>[] configGroups;

    protected volatile Properties[] properties;

    private volatile Properties defaultConfig = new Properties();

    protected volatile Set<String>[] inUseStoreKeys;

    protected volatile int latestVersion;

    private volatile boolean initialized = false; // �Ƿ��ѵ�һ�δ�Config Server�������

    /**
     * ����ͨ��addConfigEntry������ӵ���������е���������Ա�configEntries�еĸ���
     */
    private volatile Map<String, ConfigEntry> additionalConfigEntries = new ConcurrentHashMap<String, ConfigEntry>();
    /**
     * ����ͨ��addConfigEntry������ӵ������飬���е��������е���������Ա�configGroups�еĸ���
     */
    private volatile Map<String, Collection<ConfigEntry>> additionalConfigGroups = new ConcurrentHashMap<String, Collection<ConfigEntry>>();

    public void addConfigEntry(ConfigEntry configEntry) {
        ConfigUtils.addConfigEntry(configEntry, additionalConfigEntries, additionalConfigGroups);
    }


    public ConfigEntry getConfigEntry(String key, int version) {
        ConfigEntry configEntry = getFromArray(configEntries, version).get(key);
        if (configEntry != null && configEntry.isRemoved()) {
            configEntry = null;
        }
        if (configEntry == null) {
            configEntry = additionalConfigEntries.get(key);
        }
        return configEntry;
    }

    public Collection<ConfigEntry> getConfigGroup(String key, int version) {
        Map<String, ConfigEntry> groups = new ConcurrentHashMap<String, ConfigEntry>();
        addToGroups(additionalConfigGroups.get(key), groups);
        addToGroups(getFromArray(configGroups, version).get(key), groups);
        return groups.values();
    }

    private void addToGroups(Collection<ConfigEntry> source, Map<String, ConfigEntry> target) {
        if (source != null) {
            for (ConfigEntry configEntry : source) {
                target.put(configEntry.getKey(), configEntry);
            }
        }
    }

    public Collection<String> getKeys(int version) {
        Collection<String> keys = new LinkedList<String>();
        keys.addAll(additionalConfigEntries.keySet());
        keys.addAll(getFromArray(configEntries, version).keySet());
        return keys;
    }

    private <T> T getFromArray(T[] array, int version) {
        int last = array.length - 1;
        if (version > last) {
            return array[last];
        } else {
            return array[version];
        }
    }

    public void init(FilterConfig filterConfig) {
        fetchConfigServerGroup(filterConfig);
        fetchClientWaitTime(filterConfig);
        CountDownLatch configFetched = setDataObservers();
        waitFor(configFetched);
        initialized = true;
    }

    private void fetchConfigServerGroup(FilterConfig filterConfig) {
        configServerGroup = filterConfig.getInitParameter(GROUP_PARAM_NAME);
        if (StringUtils.isBlank(configServerGroup)) {
            throw new IllegalConfigException(
                    "��������tbsession���飬��Ŀ��������������daily.taobao.net��Ҫ��configServer�϶��������Ŀ�µ�xml�ļ�����������ȷ���յ�cookie�����ã�antx.properties�е����ã�taobao_common_uri_uriDynamicGroup");
        }
    }

    private void fetchClientWaitTime(FilterConfig filterConfig) {
        String  waitTime  = filterConfig.getInitParameter(CLIENT_WAIT_TIME);
        try {
        	if (StringUtils.isNotBlank(waitTime)) {
        		TIMEOUT_SECONDS = Integer.parseInt(waitTime);
        	}
        }catch (Exception e){
        	TIMEOUT_SECONDS = 30;
        }
    }
    protected abstract CountDownLatch setDataObservers();

    private void waitFor(CountDownLatch latch) {
        logger.info("�ȴ���ȡ����");
        try {
            if (latch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                logger.info("�ȴ�����");
            } else {
                logger.info("�ȴ���ʱ");
                System.exit(1); // ֱ���˳�����ֹjboss��������������˸ô���
            }
        } catch (InterruptedException e) {
            logger.error("δ֪�ж�", e);
            Thread.currentThread().interrupt();
        }
    }

//    protected void setDataObserver(String subscriberName, String dataId, CountDownLatch fetched,SubscriberDataObserver callback) {
//        SubscriberRegistration registration = new SubscriberRegistration(subscriberName, dataId);
//        registration.setCacheable(false);
//        registration.setGroup(configServerGroup);
//        Subscriber subscriber = SubscriberRegistrar.register(registration);
//        subscriber.setDataObserver(new SynchronizedDataObserver(fetched, callback));
//    }

    protected void checkLatestVersion(int latestVersion) {
        if (initialized) { // ���ǵ�һ�ν������ã����汾�Ƿ�����Ծ
            int versionDiff = latestVersion - this.latestVersion;
            if (versionDiff < 0) {
            	throw new IllegalConfigException("Config Server�������õİ汾���½�ʧ�ܣ���ͼ��version" + this.latestVersion + "����version" + latestVersion);
            }
            if (versionDiff > 1) {
                throw new IllegalConfigException("Config Server�������õİ汾���ǰ������汾��1�����Ǵ�" + this.latestVersion + "ֱ������"
                        + latestVersion + "�������������ڴ渺�����ܾ�����");
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected ConfigEntriesResult fetchConfigEntriesFromData(Collection<? extends Object> data) {
        Queue<ConfigEntry> queue = createConfigEntryQueue(data.size());
        int latestVersion = addDataToQueue(data, queue);
        Map<String, ConfigEntry>[] configEntries = new Map[latestVersion + 1];
        Map<String, Collection<ConfigEntry>>[] configGroups = new Map[latestVersion + 1];
        Set<String>[] inUseStoreKeys = new Set[latestVersion + 1];
        initConfigEntries(configEntries, configGroups, inUseStoreKeys);
        fetchConfigEntriesFromQueue(queue, configEntries, configGroups);
        fillConfigEntries(latestVersion, configEntries, configGroups);
        fetchInUseStoreKeys(configEntries, inUseStoreKeys);

        return new ConfigEntriesResult(configEntries, configGroups, inUseStoreKeys, latestVersion);
    }

    private void fetchInUseStoreKeys(Map<String, ConfigEntry>[] configEntries, Set<String>[] inUseStoreKeys) {
        for (int i = 0; i < configEntries.length; ++i) {
            for (ConfigEntry configEntry : configEntries[i].values()) {
                if (!configEntry.isRemoved()) {
                    String storeKey = configEntry.getStoreKey();
                    inUseStoreKeys[i].add(storeKey);
                }
            }
            for (ConfigEntry configEntry : additionalConfigEntries.values()) {
                String storeKey = configEntry.getStoreKey();
                inUseStoreKeys[i].add(storeKey);
            }
        }
    }

    protected int setIfBigger(int newInt, int orignalInt) {
        return newInt > orignalInt ? newInt : orignalInt;
    }

    /**
     * ֻ�����ƹ��������������ӲŻḲ�ǣ������ƺ�������������
     */
    protected <T> T[] setIfLonger(T[] newArray, T[] orignalArray) {
        return orignalArray == null || newArray.length >= orignalArray.length ? newArray : orignalArray;
    }

    private void fillConfigEntries(int latestVersion, Map<String, ConfigEntry>[] configEntries,
            Map<String, Collection<ConfigEntry>>[] configGroups) {
        for (int i = 0; i < latestVersion; ++i) {
            for (Map.Entry<String, ConfigEntry> entry : configEntries[i].entrySet()) {
                String key = entry.getKey();
                ConfigEntry configEntry = entry.getValue();

                if (!configEntries[i + 1].containsKey(key) && !configEntry.isRemoved()) {
                    ConfigUtils.addConfigEntry(configEntry, configEntries[i + 1], configGroups[i + 1]);

                }
            }
        }
    }

    private void fetchConfigEntriesFromQueue(Queue<ConfigEntry> queue, Map<String, ConfigEntry>[] configEntries,
            Map<String, Collection<ConfigEntry>>[] configGroups) {
        for (ConfigEntry currentEntry : queue) {
            int version = currentEntry.getVersion();
            if (currentEntry.isRemoved()) {
                configEntries[version].put(currentEntry.getKey(), currentEntry);
            } else {
                ConfigUtils.addConfigEntry(currentEntry, configEntries[version], configGroups[version]);
            }
        }
    }

    private void initConfigEntries(Map<String, ConfigEntry>[] configEntries,
            Map<String, Collection<ConfigEntry>>[] configGroups, Set<String>[] inUseStoreKeys) {
        for (int i = 0; i < configEntries.length; ++i) {
            configEntries[i] = new ConcurrentHashMap<String, ConfigEntry>();
        }
        for (int i = 0; i < configGroups.length; ++i) {
            configGroups[i] = new ConcurrentHashMap<String, Collection<ConfigEntry>>();
        }
        for (int i = 0; i < inUseStoreKeys.length; ++i) {
            inUseStoreKeys[i] = new ConcurrentHashSet<String>();
        }
    }

    private int addDataToQueue(Collection<? extends Object> data, Queue<ConfigEntry> queue) {
        int latestVersion = 0;
        for (Object object : data) {
            if (object instanceof Properties) {
                Properties properties = (Properties) object;
                ConfigEntry entry = populateConfigEntry(properties);
                if (entry != null) {
                    queue.add(entry);
                    latestVersion = Math.max(latestVersion, entry.getVersion());
                }
            } else {
                logger.error("����Ӧ����" + Properties.class.getName() + "���͵ģ�����ʵ����" + object.getClass().getName() + "���͵�");
            }
        }
        return latestVersion;
    }

    private Queue<ConfigEntry> createConfigEntryQueue(int size) {
        return new ArrayDeque<ConfigEntry>(size);
    }

    protected PropertiesResult fetchPropertiesFromData(Collection<? extends Object> data) {
        int latestVersion = getLatestVersion(data);
        Properties[] properties = new Properties[latestVersion + 1];
        initProperties(properties);
        fetchPropertiesFromData(data, properties);
        fillProperties(latestVersion, properties);

        return new PropertiesResult(properties, latestVersion);
    }

  protected void  fetchDefaultConfig(SpringConfigFactory factory) {
        defaultConfig = factory.getDefaultConfig();
    }
    private void fillProperties(int latestVersion, Properties[] properties) {
        for (int i = 0; i < latestVersion; ++i) {
            Properties currentProperties = properties[i];
            Properties nextProperties = properties[i + 1];
            for (String name : currentProperties.stringPropertyNames()) {
                if (!nextProperties.containsKey(name)) {
                    String value = currentProperties.getProperty(name);
                    nextProperties.setProperty(name, value);
                }
            }
        }
    }

    private void fetchPropertiesFromData(Collection<? extends Object> data, Properties[] properties) {
        for (Object object : data) {
            if (object instanceof Properties) {
                Properties currentProperties = (Properties) object;
                int version = getPropertiesVersion(currentProperties);
                properties[version].putAll(currentProperties);
            }
        }
    }

    private void initProperties(Properties[] properties) {
        for (int i = 0; i < properties.length; ++i) {
            properties[i] = new Properties();
        }
    }

    private int getLatestVersion(Collection<? extends Object> data) {
        int latestVersion = 0;
        for (Object object : data) {
            if (object instanceof Properties) {
                Properties properties = (Properties) object;
                latestVersion = Math.max(latestVersion, getPropertiesVersion(properties));
            } else {
                logger.error("����Ӧ����" + Properties.class.getName() + "���͵ģ�����ʵ����" + object.getClass().getName() + "���͵�");
            }
        }
        return latestVersion;
    }

    public Properties getProperties(int version) {
        return getFromArray(properties, version);
    }

    public Properties getDefaultConfig() {
        return defaultConfig;
    }

    public Set<String> getInUseStoreKeys(int version) {
        return getFromArray(inUseStoreKeys, version);
    }

    public int getLatestVersion() {
        return latestVersion;
    }

//    private static class SynchronizedDataObserver implements SubscriberDataObserver {
//
//        private CountDownLatch fetched;
//
//        private SubscriberDataObserver callback;
//
//        public SynchronizedDataObserver(CountDownLatch fetched, SubscriberDataObserver callback) {
//            this.fetched = fetched;
//            this.callback = callback;
//        }
//
//        public void handleData(String dataId, List<Object> data) {
//            //MonitorLog.addStat("TbSession", "ConfigServerXmlConfig", "����ConfigServer��������", 0, 1);
//
//            if (data != null && data.size() != 0) {
//                try {
//
//                	long startTime = System.currentTimeMillis();
//                	// ���ﲻ��Ҫͬ������ΪConfigClient��Զֻ��һ���߳��ڵ���SubscriberDataObserver.handleData()����
//                    callback.handleData(dataId, data);
//
//                    logger.warn("�ɹ����յ�Config Server���͵�Session������Ϣ,����ʱ��Ϊ"+(System.currentTimeMillis() - startTime)+"����");
//                    if (logger.isInfoEnabled()) {
//                        logger.info("�ɹ����յ�Config Server���͵�Session������Ϣ dataId=" + dataId+ "; XML:"
//                                + ((String) data.get(0)).replaceAll("<prop key=\"blowfish.cipherKey\">(.*)</prop>",
//                                        "<prop key=\"blowfish.cipherKey\">XXX</prop>"));
//                    }
//                } catch (Exception e) {
//                    //MonitorLog.addStat("TbSession", "ConfigServerXmlConfig", "�������ݽ����쳣", 0, 1);
//                    logger.error("���ն��ĵ�������Ϣ����ʼ�����ö���ʱ���ִ���dataId=" + dataId, e);
//                }
//            }
//            if (fetched != null) { // ����ǵ�һ�λ������
//                fetched.countDown();
//                fetched = null;
//            }
//        }
//    }

    protected static class ConfigEntriesResult {

        public Map<String, ConfigEntry>[] configEntries;

        public Map<String, Collection<ConfigEntry>>[] configGroups;

        public Set<String>[] inUseStoreKeys;

        public int latestVersion;

        public ConfigEntriesResult(Map<String, ConfigEntry>[] configEntries,Map<String, Collection<ConfigEntry>>[] configGroups, Set<String>[] inUseStoreKeys, int latestVersion) {
            this.configEntries = configEntries;
            this.configGroups = configGroups;
            this.inUseStoreKeys = inUseStoreKeys;
            this.latestVersion = latestVersion;
        }

    }

    protected static class PropertiesResult {

        public Properties[] properties;

        public int latestVersion;

        public PropertiesResult(Properties[] properties, int latestVersion) {
            this.properties = properties;
            this.latestVersion = latestVersion;
        }

    }

}
