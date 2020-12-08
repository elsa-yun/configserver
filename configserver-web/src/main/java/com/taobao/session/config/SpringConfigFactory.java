package com.taobao.session.config;

import java.util.Collection;
import java.util.Properties;

/**
 * @author hengyi
 */
public class SpringConfigFactory {

    private Properties defaultConfig;

    private Collection<Properties> combineKeyConfig;

    private Collection<Properties> configEntries;

    private Collection<Properties> properties;

    public Properties getDefaultConfig() {
        return defaultConfig;
    }

    public void setDefaultConfig(Properties defaultConfig) {
        this.defaultConfig = defaultConfig;
    }

    public Collection<Properties> getCombineKeyConfig() {
        return combineKeyConfig;
    }

    public void setCombineKeyConfig(Collection<Properties> combineKeyConfig) {
        this.combineKeyConfig = combineKeyConfig;
    }

    public void setConfigEntries(Collection<Properties> configEntries) {
        this.configEntries = configEntries;
    }

    public Collection<Properties> getConfigEntries() {
        return configEntries;
    }

    public void setProperties(Collection<Properties> properties) {
        this.properties = properties;
    }

    public Collection<Properties> getProperties() {
        return properties;
    }

}
