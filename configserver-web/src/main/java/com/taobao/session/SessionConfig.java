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
     * ��ȡsession�������
     *
     * @param key
     * @return
     */
    public ConfigEntry getConfigEntry(String key, int version);

    /**
     * ��ȡĳһ�����cookie������config����
     *
     * @param groupName
     * @return
     */
    public Collection<ConfigEntry> getConfigGroup(String key, int version);

    /**
     * ������ӹ̶������ã���sessionID������
     *
     * @param configEntry
     */
    public void addConfigEntry(ConfigEntry configEntry);

    /**
     * �����������õ�key
     *
     * @return
     */
    public Collection<String> getKeys(int version);

    /**
     * ��ʼ��
     *
     * @param filterConfig
     */
    public void init(FilterConfig filterConfig);

    /**
     * ��ȡ��������
     *
     * @param version
     * @return
     */
    public Properties getProperties(int version);

    /**
     * �����õ���store��key
     *
     * @return
     */
    public Set<String> getInUseStoreKeys(int version);

    /**
     * ��ȡ��ǰ���µİ汾��
     *
     * @return
     */
    public int getLatestVersion();
    
   
    public Properties getDefaultConfig();
}
