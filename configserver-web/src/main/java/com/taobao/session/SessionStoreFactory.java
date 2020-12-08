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
     * 生成一组新的store。如果这里成为性能瓶颈时，可以安全的改为ThreadLocal，因为所有store实现都考虑了这个情况
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
