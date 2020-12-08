package com.taobao.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import com.alibaba.common.lang.StringUtil;
import com.alibaba.common.lang.Stater;
import com.taobao.session.config.ServerSessionList;
import com.taobao.session.config.SessionCheckList;
import com.taobao.session.store.CookieStore;
import com.taobao.session.store.TairStore;
import com.taobao.session.util.UserCheckUtil;
import com.alibaba.common.lang.UniqID;

/**
 * taobao session��ʵ��
 *
 * @author huangshang, hengyi
 */
public class TaobaoSession implements HttpSession {

    private static final Logger log = Logger.getLogger(TaobaoSession.class);

    private static final Properties EMPTY_PROPERTIES = new Properties();

    public static final String SESSION_ID = "sessionID";

    public static final String SIGNATURE = SessionKeyConstants.ATTRIBUTE_SIGNATURE;

    public static final String VERSION = "version";

    private Map<String, SessionStore> storeMap;

    private  static  Map <String,String> cookiesPool = new ConcurrentHashMap<String,String> ();

    public static Map<String,String> getCookiesPool() {
		return cookiesPool;
	}

	/**
     * Ӱ�콻�׵Ĺؼ�session���ϣ���Щsession����ͬʱ�����ڿͻ��˺ͷ���ˡ�
     */

    private static List<String> affectTradeList = new ArrayList<String>();

    static{
    	affectTradeList.add("login");
    	affectTradeList.add("_nk_");
    	affectTradeList.add("userID");
    	affectTradeList.add("userIDNum");
    	affectTradeList.add("tairlastUpdatetime");
    	affectTradeList.add("_tb_token_");
    	affectTradeList.add("sellerId");
    }

    private boolean  invalidated   = false;
    /**
     * �������޸Ĺ���key�����ڷ�ֹ�汾����ʱ����д������Ա������Ը���
     */
    private Set<String> modified = new HashSet<String>();

    private Set<SessionStore> inUseStores = new HashSet<SessionStore>();

    private Map<String ,SessionStore> currentStoreMap = new HashMap<String , SessionStore>();

    /**
     * ����store��ʵ�ֿ���ʹ���˶��̣߳�����������Щ���ܱ�store���ʵ����Զ���Ҫ����volatile����
     */
    private volatile TaobaoSessionServletRequest request;

    private volatile TaobaoSessionServletResponse response;

    private volatile ServletContext context;

    private volatile SessionConfig config;

    private volatile boolean isHighVistApp ;

    private volatile String sessionId;

    private volatile int latestVersion;

    private volatile int clientVersion;

    private volatile long creationTime;

    private volatile int   maxInactiveInterval = 1800; // Ĭ�ϰ��Сʱ,��λ�� ��

    private volatile  int  maxTairExpiredInterval = 4500; // Ĭ��75���� ,��λ�� ��

    private boolean alllowForbiddenCookie = false;

    public boolean isAlllowForbiddenCookie() {
		return alllowForbiddenCookie;
	}
	public void setAlllowForbiddenCookie(boolean alllowForbiddenCookie) {
		this.alllowForbiddenCookie = alllowForbiddenCookie;
	}

	/**
     * д��cache��ÿ������session��д�붼����д�뵽���cache����ȡ��ʱ�����ȴ������ȡ
     */
    private Map<String, Object> writeCache = new HashMap<String, Object>();

    public int getMaxTairExpiredInterval() {
		return maxTairExpiredInterval;
	}
	public void setMaxTairExpiredInterval(int maxTairExpiredInterval) {
		this.maxTairExpiredInterval = maxTairExpiredInterval;
	}
	/*
     * ��ȡ��ǰsession�����õ�TairStore
     */
    public int getTairErrorCount() {
			return TairStore.getErrorCount();
	}
    public void  setTairToZero() {
		 TairStore.setErrorCount();
	}

    /*
     *  tair ʧЧʱ���Ƿ���Ҫ���¡�
     */
    private volatile boolean  expiredTimeChanged=false;

    public boolean isExpiredTimeChanged() {
		return expiredTimeChanged;
	}

	public void setExpiredTimeChanged(boolean expiredTimeChanged) {
		this.expiredTimeChanged = expiredTimeChanged;
	}

    public TaobaoSession(TaobaoSessionServletRequest request, TaobaoSessionServletResponse response,
            ServletContext context, SessionConfig config, Map<String, SessionStore> storeMap, boolean isHighVistApp) {
        this.creationTime = System.currentTimeMillis();
        this.request = request;
        this.response = response;
        this.config = config;
        this.context = context;
        this.storeMap = storeMap;
        this.isHighVistApp = isHighVistApp;

    }

    public void init() {
        setSessionToStores();
        fetchSessionId();
        fetchVersions();
        fetchInUseStores();
        fetchInCurrentStores();

    }

    private void fetchInUseStores() {
    	fetchInUseStores(clientVersion);
        if (isVersionUpdated()) {
            fetchInUseStores(latestVersion);
        }
    }

    private void fetchInCurrentStores() {
		for (SessionStore sessionStore : inUseStores) {
			currentStoreMap.put(sessionStore.getClass().toString(), sessionStore);
		}
	}

    private void fetchInUseStores(int version) {
        Set<String> inUseStoreKeys = config.getInUseStoreKeys(version);
        for (String storeKey : inUseStoreKeys) {
            SessionStore store = storeMap.get(storeKey);
            if (store != null) {
                inUseStores.add(store);
            }
        }
    }

    private void setSessionToStores() {
        for (SessionStore store : storeMap.values()) {
            store.init(this);
        }
    }


    private void fetchSessionId() {
        sessionId = (String) getAttribute(SESSION_ID);
        //�������߼����������������߼�
        if (UserCheckUtil.domainCheck(request, config)) {
             //sessionid У��
            if (StringUtils.isBlank(sessionId)) {
                sessionId = DigestUtils.md5Hex(UniqID.getInstance().getUniqID());
                setAttribute(SESSION_ID, sessionId, true);
            }
             // trickIdУ��
            String trackId = (String) getAttribute("trackid");
            if (StringUtils.isBlank(trackId)) {
                trackId = UniqID.getInstance().getUniqIDHash();
                setAttribute("trackid", trackId,true);
            }
        }
    }

    private void fetchVersions() {
        latestVersion = config.getLatestVersion();
        String latestVersionString = Integer.toString(latestVersion);
        String clientVersionString = (String) getAttribute(VERSION);
        clientVersion = NumberUtils.toInt(clientVersionString);
        // ���ԭ���İ汾�Ų��������°汾��(����ԭ���ް汾�ŵ����)��д�����°汾��
        if (!latestVersionString.equals(clientVersionString)) {
            setAttribute(VERSION, latestVersionString, true);
        }
    }


    public Object getAttribute(String name) {
    	try{
    		Object value = ObjectUtils.toString(writeCache.get(name),null);
        	if (value != null) {
        		return value;
        	}
    		// ��д�뵽�µ�λ��ʱ������λ�ö�ȡ
    		boolean readFromNew = modified.contains(name);
    		return performOnAttribute(name, true, readFromNew, new Callback() {
    			public Object perform(SessionStore store, ConfigEntry configEntry,Properties properties,TaobaoSession session) {
    			    if(isHighVistApp && UserCheckUtil.domainCheck(session.getRequest(), session.getConfig())){
    		            store = session.getStoreMap().get("cookie");
    		        }
    				if (affectTradeList.contains(configEntry.getKey().toString())&& store instanceof TairStore) {
    					try {
    						 return store.getAttribute(configEntry, properties);
    					} catch (Exception e) {
    					     log.error(new StringBuffer("Tair��ȡʧ�ܣ����Զ�ȡcookie").append(e.getMessage()).toString());
    						 SessionStore cookieStore = currentStoreMap.get(CookieStore.class.toString());
    						 return  cookieStore.getAttribute(configEntry, properties);
    					}
    				}
    					return store.getAttribute(configEntry, properties);
    			}
    		});
    	}catch (Exception e){
			log .error("Session ��ȡʧ��" + e.getMessage());
			return null;
    	}
	}

    public Object getValue(String name) {
    	return getAttribute(name);
    }

    public void setAttribute(String name, Object value) {
    	System.out.println("+++++++++++++");
    	setAttribute2WriteCache(name, value);
        setAttribute(name, value, false);
    }

    /**
     * �ǰ�������sessionд��
     * */
	private void setAttribute(String name, final Object value, boolean force) {

		// ���ڰ�������cookie �� ����ǿ��д��Ӧ�� ����ǿ��д�룬 ����д����web.xml��������ǿ��д��Ĺ��̣�����ֻ��login��mms
		try {
			if (this.isAlllowForbiddenCookie()|| !SessionCheckList.isForbidenWritten(name)) {
				setAttribute(name, value, force, true);
			} else {
				log.info("��ֹ�ǰ���Ӧ����Ҫcookieд�������(" + name + ":" + value + ")");
			}
		} catch ( Exception e) {
			log.error("sessionд��ʧ��" + e.getMessage());
		}
	}

    /**
     * д��session����Ӧ��store
     */
    private void setAttribute(String name, final Object value, boolean force, boolean setOnNew) {
        performOnAttribute(name, force, setOnNew, new Callback() {
            public Object perform(SessionStore store, ConfigEntry configEntry, Properties properties,TaobaoSession session) {
                if (affectTradeList.contains(configEntry.getKey().toString()) && store instanceof TairStore) {
                    for (SessionStore sessionStore : inUseStores) {
                        sessionStore.setAttribute(configEntry, properties, value);
                    }
                } else {
                    store.setAttribute(configEntry, properties, value);
                }
                return null;
            }
        });
    }

	public void putValue(String name, Object value) {
        setAttribute(name, value);
    }

    public void removeAttribute(String name) {
        removeAttribute(name, false);
    }

    private void removeAttribute(String name, boolean force) {
        removeAttribute(name, force, true);
    }

    private void removeAttribute(String name, boolean force, boolean removeFromNew) {
        setAttribute(name, null, force, removeFromNew);
    }

    private Object performOnAttribute(String name, boolean force, boolean performOnNew, Callback callback) {
        ConfigEntry configEntry = getConfigEntry(name, performOnNew);
        if (configEntry != null) {
            if (force || !configEntry.isReadOnly()) { // ֻ���Ƿ�ֻ�����ԣ���ǿ�Ʋ���ʱ�����ܶ����Խ��в���
                SessionStore store = getSessionStore(configEntry);
                if (store != null) {
                    // д������Ұ汾����ʱ����¼�޸ĵ����ԣ���ֹcommitʱ��д������Ա������Ը���
                    if (performOnNew && isVersionUpdated()) {
                        modified.add(name);
                    }
                    Properties properties = getProperties(performOnNew);
                    return callback.perform(store, configEntry, properties,this);
                }
            } else {
                throw new ModifyReadOnlyAttributeException("����д���ɾ��ֻ������" + name);
            }
        }
        return null;
    }

    public void removeValue(String name) {
        removeAttribute(name);
    }

    private SessionStore getSessionStore(ConfigEntry configEntry) {
        SessionStore store = storeMap.get(configEntry.getStoreKey());
        if (store != null) {
            return store;
        } else {
            log.error("δ�ҵ��洢����" + configEntry.getStoreKey());
            return null;
        }
    }

    private ConfigEntry getConfigEntry(String name, boolean performOnNew) {
        ConfigEntry configEntry = null;
        // д��ʱʹ�����°汾�����ã�����ʹ�ÿͻ���ǰ�汾������
        if (performOnNew) {
            configEntry = config.getConfigEntry(name, latestVersion);
        } else {
            configEntry = config.getConfigEntry(name, clientVersion);
        }
        if (configEntry != null) {
            return configEntry;
        } else {
        	Stater.addOne("δ�ҵ�" + name + "��������");//detail������̫����־����̫�࣬�ȹ����޸�����Monitor
            return null;
        }
    }

    private Properties getProperties(boolean performOnNew) {
        Properties properties = null;
        if (performOnNew) {
            properties = config.getProperties(latestVersion);
        } else {
            properties = config.getProperties(clientVersion);
        }
        if (properties == null) {
            properties = EMPTY_PROPERTIES;
        }
        return properties;
    }

    public Enumeration<String> getAttributeNames() {
        Collection<String> keys = config.getKeys(latestVersion); // �����°汾����
        return Collections.enumeration(keys);
    }

    public String[] getValueNames() {
        Collection<String> keys = config.getKeys(latestVersion);
        return keys.toArray(new String[keys.size()]);
    }

    public void invalidate() {
		// �Ӿɰ汾��ɾ����������
		for (String key : config.getKeys(clientVersion)) {
			ConfigEntry configEntry = config.getConfigEntry(key, clientVersion);
			if (configEntry.getLifeCycle() <= 0 && configEntry.getStoreKey() != null  && (!StringUtil.equals(configEntry.getKey(),"sessionID"))) {
				removeAttribute(key, true, false);
			}
		}

		// �汾����ʱ��ɾ��������д����°汾����
		if (isVersionUpdated()) {
			for (String key : modified) {
				ConfigEntry configEntry = config.getConfigEntry(key,latestVersion );
				if (configEntry.getLifeCycle() <= 0 && configEntry.getStoreKey() != null && (!StringUtil.equals(configEntry.getKey(),"sessionID"))) {
					removeAttribute(key, true);
				}
			}
		}
		invalidated = true;
	}

    /**
     * �жϵ�ǰsession�Ƿ�Ƿ���
     */
    public boolean isInvalidated() {
        return invalidated;
    }

    private void setNewSessionId(){
        //����sessionId

    }
    public void commit() {
    	try{

            if (isVersionUpdated()) {
                migrateAttributes();
            }

            for (SessionStore store : inUseStores) {
            	if(store instanceof TairStore){
            		((TairStore) store).setExpiredTimeChanged(isExpiredTimeChanged());
            	}
                store.commit();
            }
    	}catch (Exception e){
    		log.error("session commit failure��" + e.getMessage());
    	}
    }
	private void setAttribute2WriteCache(String name, Object value) {
		if (value != null) {
        	writeCache.put(name, value);
        }
	}
    private void migrateAttributes() {
        Properties clientProperties = config.getProperties(clientVersion);
        Properties latestProperties = config.getProperties(latestVersion);
        String clientPropertiesVersion = clientProperties.getProperty(VERSION);
        String latestPropertiesVersion = latestProperties.getProperty(VERSION);
        // properties����ʱ��Ҫ�����������ԣ���Ϊ�޷�ȷ����Щ����ʱӦ�ñ����µ�
        boolean migrateAll = !StringUtils.equals(clientPropertiesVersion, latestPropertiesVersion);

        for (String key : config.getKeys(clientVersion)) {
            ConfigEntry clientConfigEntry = config.getConfigEntry(key, clientVersion);
            ConfigEntry latestConfigEntry = config.getConfigEntry(key, latestVersion);
            if (latestConfigEntry == null) { // �ѱ�ɾ��
                removeAttribute(key, true, false); // �Ƴ��ɵ�ֵ
            } else if (migrateAll || clientConfigEntry.getVersion() != latestConfigEntry.getVersion()) { // �и���
                Object value = getAttribute(key); // �Ȼ�ȡ�ɵ�ֵ�������д����ֵ���ȡ�µ�ֵ
                removeAttribute(key, true, false); // �Ƴ��ɵ�ֵ
                setAttribute(key, value, true); // д���µ�ֵ
            }
        }
    }

    private boolean isVersionUpdated() {
        return clientVersion != latestVersion;
    }

    public long getCreationTime() {
        return this.creationTime;
    }

    public String getId() {
        return this.sessionId;
    }

    public long getLastAccessedTime() {
        return this.creationTime;
    }

    public int getMaxInactiveInterval() {
        return this.maxInactiveInterval;
    }

    public ServletContext getServletContext() {
        return this.context;
    }

    @Deprecated
    public javax.servlet.http.HttpSessionContext getSessionContext() {
        throw new UnsupportedOperationException();
    }

    public boolean isNew() {
        return true;
    }

    public void setMaxInactiveInterval(int interval) {
        this.maxInactiveInterval = interval;
    }

    public TaobaoSessionServletRequest getRequest() {
        return request;
    }

    public TaobaoSessionServletResponse getResponse() {
        return response;
    }

    public SessionConfig getConfig() {
        return config;
    }

    public Map<String, SessionStore> getStoreMap() {
        return storeMap;
    }

    public int getClientVersion() {
        return clientVersion;
    }

    public int getLatestVersion() {
        return latestVersion;
    }

    public Properties getClientVersionProperties() {
        return config.getProperties(clientVersion);
    }

    public Properties getLatestVersionProperties() {
        return config.getProperties(latestVersion);
    }

    private static interface Callback {
        public Object perform(SessionStore store, ConfigEntry configEntry, Properties properties,TaobaoSession session);
    }

    public void cookieCollect() {
		try {
			Set<String> nickKeySet = ServerSessionList.getNickKeySet();
			Cookie[] cookieList = this.request.getCookies();
			 for (Cookie cookie : cookieList) {
                 if( cookiesPool.size() >= 2000 ){
                     break;
                 }
                 if ( !nickKeySet.contains(cookie.getName())) {
                         cookiesPool.put(cookie.getName(), cookie.getValue()+"   "+cookie.getDomain());
                 }
         }
		} catch (Exception e) {
		}
	}

  public String encodeValue(String name, String value) {
        try {
            ConfigEntry configEntry = getConfigEntry(name, true);
            Properties properties = getProperties(true);
            return CookieStore.encodeValue(value, configEntry, properties);
        } catch (Exception e) {
            return null;
        }
    }
 public static void main(String args[]){

    String url = "http://list.mall.daily.taobao.net/search_product.htm?f=D9_5_1&user_action=initiative&at_topsearch=1&search_type=auction&commend=all&atype=b&q=%CA%D6%BB%FA&ssid=s1-e&isnew=2";
    Pattern p = Pattern.compile("(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)",Pattern.CASE_INSENSITIVE);
    Matcher matcher = p.matcher(url);
    matcher.find();
    if (StringUtil.equals(matcher.group(), "taobao.com") ||StringUtil.equals(matcher.group(), "taobao.net")){
        System.out.println( matcher.group());
    }

 }


}
