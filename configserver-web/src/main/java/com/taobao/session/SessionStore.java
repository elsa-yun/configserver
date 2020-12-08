package com.taobao.session;

import java.util.Properties;

/**
 * @author huangshang, hengyi
 */
public interface SessionStore {

    /**
     * 获取属性值
     *
     * @param configEntry
     * @param properties
     * @return
     */
    public Object getAttribute(ConfigEntry configEntry, Properties properties);

    /**
     * 设置属性值
     *
     * @param configEntry
     * @param properties
     * @param value 为null时表示删除
     */
    public void setAttribute(ConfigEntry configEntry, Properties properties, Object value);

    /**
     * 提交修改
     */
    public void commit();

    /**
     * 初始化
     *
     * @param session
     */
    public void init(TaobaoSession session);

    /**
     * 会话已初始化完成，tair实现需要这个方法来从tair读取数据
     */
    public void onSessionReady();

}
