package com.taobao.session.config;

import static com.taobao.session.util.ConfigUtils.fetchConfigEntriesFromFactory;
import static com.taobao.session.util.ConfigUtils.getPropertiesVersion;
import static com.taobao.session.util.ConfigUtils.getSpringConfigFactoryFromContext;
import static com.taobao.session.util.ConfigUtils.populateConfigEntry;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.FilterConfig;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.taobao.session.ConfigEntry;
import com.taobao.session.IllegalConfigException;
import com.taobao.session.SessionConfig;
import com.taobao.session.util.ConfigUtils;

/**
 * @author hengyi
 */
public class SpringConfig implements SessionConfig {

    private static final String SPRING_CONFIG_LOCATIONS = "springConfigLocations";

    private volatile Map<String, ConfigEntry> configEntries = new HashMap<String, ConfigEntry>();

    private volatile Map<String, Collection<ConfigEntry>> configGroups = new HashMap<String, Collection<ConfigEntry>>();

    private volatile Properties properties = new Properties();

    private volatile Properties defaultConfig = new Properties();

    private volatile Set<String> inUseStoreKeys = new HashSet<String>();

    public void addConfigEntry(ConfigEntry configEntry) {
        ConfigUtils.addConfigEntry(configEntry, configEntries, configGroups);
    }

    public ConfigEntry getConfigEntry(String key, int version) {
        return configEntries.get(key);
    }

    public Collection<ConfigEntry> getConfigGroup(String key, int version) {
        return configGroups.get(key);
    }

    public Collection<String> getKeys(int version) {
        return configEntries.keySet();
    }

    public void init(FilterConfig filterConfig) {
        ApplicationContext context = getApplicationContext(filterConfig);
        fetchConfigEntries(context);
    }

    private void fetchConfigEntries(ApplicationContext context) {
        SpringConfigFactory factory = getSpringConfigFactoryFromContext(context);
        fetchConfigEntriesWithFactory(factory);
    }

    private void fetchConfigEntriesWithFactory(SpringConfigFactory factory) {
        fetchConfigEntries(factory);
        fetchProperties(factory);
        fetchInUseStoreKeys();
    }

    private void fetchInUseStoreKeys() {
        for (ConfigEntry configEntry : configEntries.values()) {
            String storeKey = configEntry.getStoreKey();
            inUseStoreKeys.add(storeKey);
        }
    }

    private void fetchConfigEntries(SpringConfigFactory factory) {
        Collection<Properties> entries = fetchConfigEntriesFromFactory(factory);
        if (null != entries && entries.size() > 0) {
            Map<String, ConfigEntry> configEntries = new HashMap<String, ConfigEntry>();
            for (Properties entryProperties : entries) {
                ConfigEntry configEntry = populateConfigEntry(entryProperties);
                String key = configEntry.getKey();
                ConfigEntry original = configEntries.get(key);
                if (original == null || configEntry.getVersion() > original.getVersion()) { // 使用最新版本的配置
                    configEntries.put(key, configEntry);
                }
            }
            for (ConfigEntry configEntry : configEntries.values()) {
                if (!configEntry.isRemoved()) {
                    addConfigEntry(configEntry);
                }
            }
        } else {
            throw new IllegalConfigException("必须包含对configEntries的配置");
        }
    }

    private void fetchProperties(SpringConfigFactory factory) {
        Collection<Properties> properties = factory.getProperties();
        if (properties != null && properties.size() > 0) {
            for (Properties currentProperties : properties) {
                int thisVersion = getPropertiesVersion(this.properties);
                int currentVersion = getPropertiesVersion(currentProperties);
                if (currentVersion >= thisVersion) {
                    this.properties = currentProperties;
                }
            }
        }
    }

    private ApplicationContext getApplicationContext(FilterConfig filterConfig) {
        String param = filterConfig.getInitParameter(SPRING_CONFIG_LOCATIONS);
        System.out.println(param);
        String[] springConfigLocations = ConfigUtils.splitConfig(param);
        ApplicationContext context = null;
        if (!ArrayUtils.isEmpty(springConfigLocations)) {
            context = new ClassPathXmlApplicationContext(springConfigLocations);
        } else {
            context = WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext());
        }
        return context;
    }

    public Properties getProperties(int version) {
        return properties;
    }

    public int getLatestVersion() {
        return 0;
    }

    public Set<String> getInUseStoreKeys(int version) {
        return inUseStoreKeys;
    }


    public Properties getDefaultConfig() {
        return defaultConfig;
    }

}
