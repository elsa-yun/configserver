package com.taobao.session;

import java.util.Collection;
import java.util.Properties;
import java.util.Set;

import javax.servlet.FilterConfig;

/**
 * @author huangshang, hengyi
 */
public interface SessionConfig {

    /**
     * 获取session项的配置
     *
     * @param key
     * @return
     */
    public ConfigEntry getConfigEntry(String key, int version);

    /**
     * 获取某一个组合cookie的所有config内容
     *
     * @param groupName
     * @return
     */
    public Collection<ConfigEntry> getConfigGroup(String key, int version);

    /**
     * 用于添加固定的配置，如sessionID的配置
     *
     * @param configEntry
     */
    public void addConfigEntry(ConfigEntry configEntry);

    /**
     * 返回所有配置的key
     *
     * @return
     */
    public Collection<String> getKeys(int version);

    /**
     * 初始化
     *
     * @param filterConfig
     */
    public void init(FilterConfig filterConfig);

    /**
     * 获取配置属性
     *
     * @param version
     * @return
     */
    public Properties getProperties(int version);

    /**
     * 返回用到的store的key
     *
     * @return
     */
    public Set<String> getInUseStoreKeys(int version);

    /**
     * 获取当前最新的版本号
     *
     * @return
     */
    public int getLatestVersion();
    
   
    public Properties getDefaultConfig();
}
