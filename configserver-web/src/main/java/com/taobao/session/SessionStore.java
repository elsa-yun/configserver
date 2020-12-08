package com.taobao.session;

import java.util.Properties;

/**
 * @author huangshang, hengyi
 */
public interface SessionStore {

    /**
     * ��ȡ����ֵ
     *
     * @param configEntry
     * @param properties
     * @return
     */
    public Object getAttribute(ConfigEntry configEntry, Properties properties);

    /**
     * ��������ֵ
     *
     * @param configEntry
     * @param properties
     * @param value Ϊnullʱ��ʾɾ��
     */
    public void setAttribute(ConfigEntry configEntry, Properties properties, Object value);

    /**
     * �ύ�޸�
     */
    public void commit();

    /**
     * ��ʼ��
     *
     * @param session
     */
    public void init(TaobaoSession session);

    /**
     * �Ự�ѳ�ʼ����ɣ�tairʵ����Ҫ�����������tair��ȡ����
     */
    public void onSessionReady();

}
