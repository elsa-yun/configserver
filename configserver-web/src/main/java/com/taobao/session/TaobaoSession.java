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
 * taobao session的实现
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
     * 影响交易的关键session集合，这些session数据同时保存在客户端和服务端。
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
     * 保存已修改过的key，用于防止版本升级时，新写入的属性被旧属性覆盖
     */
    private Set<String> modified = new HashSet<String>();

    private Set<SessionStore> inUseStores = new HashSet<SessionStore>();

    private Map<String ,SessionStore> currentStoreMap = new HashMap<String , SessionStore>();

    /**
     * 由于store的实现可能使用了多线程，所以下面这些可能被store访问的属性都需要加上volatile修饰
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

    private volatile int   maxInactiveInterval = 1800; // 默认半个小时,单位： 秒

    private volatile  int  maxTairExpiredInterval = 4500; // 默认75分钟 ,单位： 秒

    private boolean alllowForbiddenCookie = false;

    public boolean isAlllowForbiddenCookie() {
		return alllowForbiddenCookie;
	}
	public void setAlllowForbiddenCookie(boolean alllowForbiddenCookie) {
		this.alllowForbiddenCookie = alllowForbiddenCookie;
	}

	/**
     * 写入cache，每个请求session的写入都会先写入到这个cache，读取的时候优先从这里读取
     */
    private Map<String, Object> writeCache = new HashMap<String, Object>();

    public int getMaxTairExpiredInterval() {
		return maxTairExpiredInterval;
	}
	public void setMaxTairExpiredInterval(int maxTairExpiredInterval) {
		this.maxTairExpiredInterval = maxTairExpiredInterval;
	}
	/*
     * 获取当前session中在用的TairStore
     */
    public int getTairErrorCount() {
			return TairStore.getErrorCount();
	}
    public void  setTairToZero() {
		 TairStore.setErrorCount();
	}

    /*
     *  tair 失效时间是否需要更新。
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
        //老域名逻辑，新域名无以下逻辑
        if (UserCheckUtil.domainCheck(request, config)) {
             //sessionid 校验
            if (StringUtils.isBlank(sessionId)) {
                sessionId = DigestUtils.md5Hex(UniqID.getInstance().getUniqID());
                setAttribute(SESSION_ID, sessionId, true);
            }
             // trickId校验
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
        // 如果原来的版本号不等于最新版本号(包括原来无版本号的情况)，写入最新版本号
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
    		// 已写入到新的位置时，从新位置读取
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
    					     log.error(new StringBuffer("Tair读取失败，尝试读取cookie").append(e.getMessage()).toString());
    						 SessionStore cookieStore = currentStoreMap.get(CookieStore.class.toString());
    						 return  cookieStore.getAttribute(configEntry, properties);
    					}
    				}
    					return store.getAttribute(configEntry, properties);
    			}
    		});
    	}catch (Exception e){
			log .error("Session 读取失败" + e.getMessage());
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
     * 非白名单的session写入
     * */
	private void setAttribute(String name, final Object value, boolean force) {

		// 不在白名单的cookie 和 允许强制写的应用 可以强制写入， 允许写入在web.xml中配置了强制写入的工程，现在只有login和mms
		try {
			if (this.isAlllowForbiddenCookie()|| !SessionCheckList.isForbidenWritten(name)) {
				setAttribute(name, value, force, true);
			} else {
				log.info("禁止非白名应用重要cookie写入操作：(" + name + ":" + value + ")");
			}
		} catch ( Exception e) {
			log.error("session写入失败" + e.getMessage());
		}
	}

    /**
     * 写入session到相应的store
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
            if (force || !configEntry.isReadOnly()) { // 只有是非只读属性，或强制操作时，才能对属性进行操作
                SessionStore store = getSessionStore(configEntry);
                if (store != null) {
                    // 写入操作且版本升级时，记录修改的属性，防止commit时新写入的属性被旧属性覆盖
                    if (performOnNew && isVersionUpdated()) {
                        modified.add(name);
                    }
                    Properties properties = getProperties(performOnNew);
                    return callback.perform(store, configEntry, properties,this);
                }
            } else {
                throw new ModifyReadOnlyAttributeException("不能写入或删除只读属性" + name);
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
            log.error("未找到存储类型" + configEntry.getStoreKey());
            return null;
        }
    }

    private ConfigEntry getConfigEntry(String name, boolean performOnNew) {
        ConfigEntry configEntry = null;
        // 写入时使用最新版本的配置，否则使用客户当前版本的配置
        if (performOnNew) {
            configEntry = config.getConfigEntry(name, latestVersion);
        } else {
            configEntry = config.getConfigEntry(name, clientVersion);
        }
        if (configEntry != null) {
            return configEntry;
        } else {
        	Stater.addOne("未找到" + name + "的配置项");//detail并发量太大，日志不能太多，等哈勃修复了用Monitor
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
        Collection<String> keys = config.getKeys(latestVersion); // 按最新版本返回
        return Collections.enumeration(keys);
    }

    public String[] getValueNames() {
        Collection<String> keys = config.getKeys(latestVersion);
        return keys.toArray(new String[keys.size()]);
    }

    public void invalidate() {
		// 从旧版本中删除所有属性
		for (String key : config.getKeys(clientVersion)) {
			ConfigEntry configEntry = config.getConfigEntry(key, clientVersion);
			if (configEntry.getLifeCycle() <= 0 && configEntry.getStoreKey() != null  && (!StringUtil.equals(configEntry.getKey(),"sessionID"))) {
				removeAttribute(key, true, false);
			}
		}

		// 版本升级时，删除所有已写入的新版本属性
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
     * 判断当前session是否非法。
     */
    public boolean isInvalidated() {
        return invalidated;
    }

    private void setNewSessionId(){
        //生成sessionId

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
    		log.error("session commit failure：" + e.getMessage());
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
        // properties升级时需要更新所有属性，因为无法确定哪些属性时应该被更新的
        boolean migrateAll = !StringUtils.equals(clientPropertiesVersion, latestPropertiesVersion);

        for (String key : config.getKeys(clientVersion)) {
            ConfigEntry clientConfigEntry = config.getConfigEntry(key, clientVersion);
            ConfigEntry latestConfigEntry = config.getConfigEntry(key, latestVersion);
            if (latestConfigEntry == null) { // 已被删除
                removeAttribute(key, true, false); // 移除旧的值
            } else if (migrateAll || clientConfigEntry.getVersion() != latestConfigEntry.getVersion()) { // 有更新
                Object value = getAttribute(key); // 先获取旧的值，如果已写入新值则读取新的值
                removeAttribute(key, true, false); // 移除旧的值
                setAttribute(key, value, true); // 写入新的值
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
