package com.taobao.session;

import java.util.HashMap;
import java.util.Map;

import com.taobao.session.util.ClassUtils;

/**
 * @author huangshang, hengyi
 */
public class SessionStoreFactory {

    private Map<String, Class<? extends SessionStore>> storeTypeMap;

    public void setStoreTypeMap(Map<String, Class<? extends SessionStore>> storeTypeMap) {
        this.storeTypeMap = storeTypeMap;
    }

    public Map<String, Class<? extends SessionStore>> getStoreTypeMap() {
        return storeTypeMap;
    }

    /**
     * ����һ���µ�store����������Ϊ����ƿ��ʱ�����԰�ȫ�ĸ�ΪThreadLocal����Ϊ����storeʵ�ֶ�������������
     *
     * @param request
     * @param response
     * @return
     */
    public Map<String, SessionStore> createStoreMap() {
        Map<String, SessionStore> result = new HashMap<String, SessionStore>();
        for (Map.Entry<String, Class<? extends SessionStore>> entry : storeTypeMap.entrySet()) {
            String storeName = entry.getKey();
            Class<? extends SessionStore> storeClass = entry.getValue();

            SessionStore store = ClassUtils.newInstance(storeClass);
            result.put(storeName, store);
        }
        return result;
    }

}
