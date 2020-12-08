package com.taobao.session.util;

import static com.taobao.session.TaobaoSession.VERSION;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.taobao.session.ConfigEntry;
import com.taobao.session.IllegalConfigException;
import com.taobao.session.config.ServerSessionList;
import com.taobao.session.config.SpringConfigFactory;

/**
 * @author hengyi
 */
public class ConfigUtils {

    private static final Logger logger = Logger.getLogger(ConfigUtils.class);

    private static final Properties EMPTY_PROPERTIES = new Properties();

    private static final String CONFIG_SEPARATORS = ",;";

    private static final String COMPRESS_KEY = "compressKey";

    private static final Map<String, String> DEFAULT_COMBINE_KEY_CONFIG = new HashMap<String, String>();

    static {
        DEFAULT_COMBINE_KEY_CONFIG.put("lifeCycle", "-1");
        DEFAULT_COMBINE_KEY_CONFIG.put("domain", "");
        DEFAULT_COMBINE_KEY_CONFIG.put("cookiePath", "/");
        DEFAULT_COMBINE_KEY_CONFIG.put("httpOnly", "false");
    }

    public static void addConfigEntry(ConfigEntry configEntry, Map<String, ConfigEntry> configEntries,
            Map<String, Collection<ConfigEntry>> configGroups) {
        if (configEntry != null) {
            String key = configEntry.getKey();
			logger.error("before addConfigEntry key ===>" + key + "::::::" + configEntry.toString());
            if (StringUtils.isNotBlank(key)) {
                if (!configEntries.containsKey(key)) {
                    if (StringUtils.isNotBlank(configEntry.getStoreKey())) {
                        configEntries.put(key, configEntry);
                        logger.error("addConfigEntry key ===>"+key);
                        addToConfigGroup(configEntry, configGroups);
                    } else {
                        throw new IllegalConfigException("����ָ��������" + key + "��storeKey");
                    }
                } else {
                    throw new IllegalConfigException(key + "���������Ѵ���");
                }
            } else {
                throw new IllegalConfigException("����ָ���������key");
            }
        } else {
            throw new IllegalConfigException("�������Ϊnull");
        }
    }

    public static void addToConfigGroup(ConfigEntry configEntry, Map<String, Collection<ConfigEntry>> configGroups) {
        String compressKey = configEntry.getCompressKey();
        if (StringUtils.isNotBlank(compressKey)) {
            Collection<ConfigEntry> configGroup = configGroups.get(compressKey);
            if (configGroup == null) {
                configGroup = new ConcurrentLinkedQueue<ConfigEntry>();
                configGroups.put(compressKey, configGroup);
            }
            configGroup.add(configEntry);
        }
    }

    public static ConfigEntry populateConfigEntry(Properties properties) {
        ConfigEntry configEntry = new ConfigEntry();
        try {
            BeanUtils.populate(configEntry, properties);
            return configEntry;
        } catch (Exception e) {
            logger.error("�������õ�Entryʧ��" + properties, e);
            throw new IllegalConfigException("�������õ�Entryʧ��" + properties, e);
        }
    }

    public static SpringConfigFactory getSpringConfigFactoryFromContext(ApplicationContext context) {
        @SuppressWarnings("unchecked")
        Map<String, SpringConfigFactory> beans = context.getBeansOfType(SpringConfigFactory.class);
        if (beans.size() == 0) {
            throw new IllegalConfigException("û���ҵ��κ�����Ϊ" + SpringConfigFactory.class.getName() + "��bean����");
        } else if (beans.size() > 1) {
            throw new IllegalConfigException("�ҵ�����һ������Ϊ" + SpringConfigFactory.class.getName() + "��bean����");
        } else {
            SpringConfigFactory factory = beans.values().toArray(new SpringConfigFactory[1])[0];
            return factory;
        }
    }

    public static Collection<Properties> fetchConfigEntriesFromFactory(SpringConfigFactory factory) {
        Collection<Properties> configEntries = factory.getConfigEntries();
        // XXX ע�⣬��֧��defaultConfig��combineKeyConfig�İ汾��
        Properties defaultConfig = defaultIfNull(factory.getDefaultConfig(), EMPTY_PROPERTIES);
        Collection<Properties> combineKeyConfig = factory.getCombineKeyConfig();
        Map<String, Properties> combineKeyConfigMap = convertCombineKeyConfigToMap(combineKeyConfig);

        Collection<Properties> result = null;
        if (configEntries!=null && configEntries.size() > 0) {
            result = new ArrayList<Properties>(configEntries.size());
            for (Properties properties : configEntries) {
                Properties config = new Properties();
                ServerSessionList.getNickKeySet().add(properties.getProperty("nickKey"));
                config.putAll(defaultConfig);
                config.putAll(properties);
                applyCombineKeyConfig(config, combineKeyConfigMap, defaultConfig);
                result.add(config);
            }
        }

        return result;
    }

    private static void applyCombineKeyConfig(Properties config, Map<String, Properties> combineKeyConfigMap,
            Properties defaultConfig) {
        String compressKey = config.getProperty(COMPRESS_KEY);
        ServerSessionList.getNickKeySet().add(compressKey);
        if (compressKey != null) {
            Properties combineKeyConfig = defaultIfNull(combineKeyConfigMap.get(compressKey), EMPTY_PROPERTIES);
            for (Map.Entry<String, String> entry : DEFAULT_COMBINE_KEY_CONFIG.entrySet()) {
                String name = entry.getKey();
                
                String defaultValue = entry.getValue();
                String value = combineKeyConfig.getProperty(name, defaultConfig.getProperty(name, defaultValue));
                config.put(name, value);
            }
        }
    }

    private static Map<String, Properties> convertCombineKeyConfigToMap(Collection<Properties> combineKeyConfig) {
        Map<String, Properties> combineKeyConfigMap = new HashMap<String, Properties>();
        if (combineKeyConfig != null && combineKeyConfig.size() > 0) {
            for (Properties config : combineKeyConfig) {
                combineKeyConfigMap.put(config.getProperty(COMPRESS_KEY), config);
            }
        }
        return combineKeyConfigMap;
    }

    private static <T> T defaultIfNull(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static String[] splitConfig(String config) {
        return StringUtils.split(config, CONFIG_SEPARATORS);
    }

    public static int getPropertiesVersion(Properties properties) {
        return NumberUtils.toInt(properties.getProperty(VERSION));
    }

}
