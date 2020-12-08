package com.taobao.session.store;

import java.util.Properties;

import com.taobao.session.ConfigEntry;

/**
 * @author hengyi
 */
public class Attribute {

    private ConfigEntry configEntry;

    private Properties properties;

    private Object value;

    public Attribute(ConfigEntry configEntry, Properties properties) {
        this(configEntry, properties, null);
    }

    public Attribute(ConfigEntry configEntry, Properties properties, Object value) {
        this.configEntry = configEntry;
        this.properties = properties;
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public ConfigEntry getConfigEntry() {
        return configEntry;
    }

    public Properties getProperties() {
        return properties;
    }

}
