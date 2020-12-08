package com.taobao.session.store;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.Cookie;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.session.ConfigEntry;
import com.taobao.session.IllegalConfigException;
import com.taobao.session.SessionConfig;
import com.taobao.session.SessionStore;
import com.taobao.session.TaobaoCookie;
import com.taobao.session.TaobaoSession;
import com.taobao.session.TaobaoSessionServletRequest;
import com.taobao.session.TaobaoSessionServletResponse;
import com.taobao.session.util.Base64Utils;
import com.taobao.session.util.BlowfishUtils;
import com.taobao.session.util.EnvSessionUtil;

/**
 * @author hengyi
 */
public class CookieStore implements SessionStore {

    private static final Logger logger = Logger.getLogger(CookieStore.class);
    
    private static final String BLOWFISH_CIPHER_KEY = "blowfish.cipherKey";

    private static final String DEFAULT_COOKIE_PATH = "/";

    private static final String COMBINE_SEPARATOR = "&";

    private static final String KEY_VALUE_SEPARATOR = "=";

    private static final String URL_ENCODING = "UTF-8";

    /**
     * 用于标记编码失败
     */
    private static final String ERROR = new String("ERROR");

    /**
     * 存放已解析的属性
     */
    private Map<String, Attribute> attributes;

    /**
     * 存放未解析的属性
     */
    private Map<String, String> cookies;

    /**
     * 标记修改过的属性
     */
    private Set<String> dirty;

    private TaobaoSession session;

    public void commit() {
        String[] originalDirty = dirty.toArray(new String[dirty.size()]);
        for (String key : originalDirty) {
            if (dirty.contains(key)) { // 该key可能已经在之前的组合cookie中处理过，因此需要先检查是否还在dirty中
                Attribute attribute = attributes.get(key);
                ConfigEntry configEntry = attribute.getConfigEntry();
                Properties properties = attribute.getProperties();
                encodeCookie(configEntry, properties);
            }
        }
    }

    private void encodeCookie(ConfigEntry configEntry, Properties properties) {
        if (configEntry.isCompress()) {
            encodeCompressCookie(configEntry, properties);
        } else {
            encodeSingleCookie(configEntry, properties);
        }
    }

    private void encodeCompressCookie(ConfigEntry configEntry, Properties properties) {
        String compressKey = configEntry.getCompressKey();

        SessionConfig config = getConfig();
        Collection<ConfigEntry> configGroup = config.getConfigGroup(compressKey, getLatestVersion());
        if (configGroup == null || configGroup.size() == 0) {
        	logger.warn("configGroup不应该为空，请检查" + config.getClass().getName() + "的实现");
        } else {
            String compressValue = buildCompressValue(configGroup, properties);
            addCookieToResponse(configEntry, compressKey, compressValue, StringUtils.isBlank(compressValue));
        }
    }

    private String buildCompressValue(Collection<ConfigEntry> configGroup, Properties properties) {
        StringBuilder compressBuilder = new StringBuilder();
        boolean first = true;
        for (ConfigEntry thisConfigEntry : configGroup) {
            String nickKey = thisConfigEntry.getNickKey();
            Object attribute = getAttribute(thisConfigEntry); // 为null时表示该属性已被删除
            String value = attribute != null ? attribute.toString() : null;
            value = encodeValue(value, thisConfigEntry, properties);
            // 防止多次保存cookie
            removeFromDirty(thisConfigEntry);
            if (value == null || value == ERROR) { // 本属性已被删除或编码错误时忽略当前属性
                continue;
            }
            if (first) {
                first = false;
            } else {
                compressBuilder.append(COMBINE_SEPARATOR);
            }
            compressBuilder.append(nickKey).append(KEY_VALUE_SEPARATOR).append(value);
        }
        return compressBuilder.toString();
    }

    private void removeFromDirty(ConfigEntry thisConfigEntry) {
        String key = thisConfigEntry.getKey();
        dirty.remove(key);
    }

    private void encodeSingleCookie(ConfigEntry configEntry, Properties properties) {
        String name = configEntry.getNickKey();
        Object attribute = getAttribute(configEntry); // 为null时表示该属性已被删除
        String value = attribute != null ? attribute.toString() : StringUtils.EMPTY;
        value = encodeValue(value, configEntry, properties);
        if (value == ERROR) { // 编码错误时忽略当前cookie
            return;
        }
        addCookieToResponse(configEntry, name, value, attribute == null);
    }

    public static String encodeValue(String value, ConfigEntry configEntry, Properties properties) {
        if (StringUtils.isEmpty(value)) {
            return value;
        }
        if (configEntry.isEscapeJava()) {
            value = StringEscapeUtils.escapeJava(value);
        } else if (configEntry.isEncrypt()) {
            if (configEntry.isBase64()) {
                value = Base64Utils.addBase64Head(value);
            }
            value = BlowfishUtils.encryptBlowfish(value, getBlowfishKey(properties));
        } else if (configEntry.isBase64()) {
            value = Base64Utils.encodeBase64(value);
        }
        try {
            // XXX 总是编码，与原实现不同，原实现在组合cookie时只对加密的属性编码
            value = URLEncoder.encode(value, URL_ENCODING);
        } catch (Exception e) {
            logger.error("编码失败", e);
            //MonitorLog.addStat("TbSession", "CookieStore", "singleCookie编码失败", 0, 1);
            // 编码失败时，返回错误标记，且不保存到cookies中
            return ERROR;
        }
        return value;
    }

    private void addCookieToResponse(ConfigEntry configEntry, String name, String value, boolean removed) {
        // 只刪除已存在的cookie
        if (removed && !cookies.containsKey(name)) {
            return;
        }

        String domain = configEntry.getDomain();
        // XXX 删除cookie时将maxAge设为0，但原来的实现只是将值设为空而已
        int maxAge = !removed ? configEntry.getLifeCycle() : 0;
        String path = configEntry.getCookiePath();
        boolean httpOnly = configEntry.isHttpOnly();
        addCookieToResponse(name, value, domain, maxAge, path, httpOnly);
    }

    private void addCookieToResponse(String name, String value, String domain, int maxAge, String path, boolean httpOnly) {
        TaobaoSessionServletResponse response = getResponse();
        TaobaoCookie cookie = buildCookie(name, value, domain, maxAge, path, httpOnly);
        response.addCookie(cookie);
    }

    private TaobaoCookie buildCookie(String name, String value, String domain, int maxAge, String path, boolean httpOnly) {
        TaobaoCookie cookie = new TaobaoCookie(name, value);
        if (StringUtils.isNotBlank(domain)) {
            cookie.setDomain(domain);
        }
        if (StringUtils.isNotBlank(path)) {
            cookie.setPath(path);
        } else {
            cookie.setPath(DEFAULT_COOKIE_PATH);
        }
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(httpOnly);
        return cookie;
    }

    public Object getAttribute(ConfigEntry configEntry, Properties properties) {
        //MonitorLog.addStat("TbSession", "CookieStore", "getAttribute", 0, 1);
        String key = configEntry.getKey();
        Attribute attribute = attributes.get(key);

        if (attribute == null) { // 属性未解析过
            decodeCookie(configEntry, properties);
            // 再次查找属性
            attribute = attributes.get(key);
        }

        // XXX value为null时要改为空串(与旧实现兼容?)

        return attribute.getValue();
    }

    private Object getAttribute(ConfigEntry configEntry) {
        return getAttribute(configEntry, getClientVersionProperties());
    }

    private void decodeCookie(ConfigEntry configEntry, Properties properties) {
        if (configEntry.isCompress()) {
            decodeCompressCookie(configEntry, properties);
        } else {
            decodeSingleCookie(configEntry, properties, cookies);
        }
    }

    private void decodeCompressCookie(ConfigEntry configEntry, Properties properties) {
        String compressKey = configEntry.getCompressKey();
        String cookieValue = cookies.get(compressKey);

        Map<String, String> separateCookies = separateCookies(cookieValue);

        SessionConfig config = getConfig();
        Collection<ConfigEntry> configGroup = config.getConfigGroup(compressKey, getClientVersion()); // 按客户端当前版本解析
        if (configGroup != null && configGroup.size() > 0) {
            for (ConfigEntry thisConfigEntry : configGroup) {
                if (!dirty.contains(thisConfigEntry.getKey())) { // 如果该属性没有被写入过，才进行解码，否则原来写入的值会被覆盖
                    decodeSingleCookie(thisConfigEntry, properties, separateCookies);
                }
            }
        } else {
            logger.warn("configGroup不应该为空，请检查" + config.getClass().getName() + "的实现");
        }
    }

    private Map<String, String> separateCookies(String cookieValue) {
        Map<String, String> separateCookies = new HashMap<String, String>();

        String[] contents = StringUtils.split(cookieValue, COMBINE_SEPARATOR);
        if (!ArrayUtils.isEmpty(contents)) {
            for (String content : contents) {
                String[] keyValue = StringUtils.split(content, KEY_VALUE_SEPARATOR, 2);
                if (ArrayUtils.getLength(keyValue) == 2) {
                    String key = keyValue[0];
                    String value = keyValue[1];
                    separateCookies.put(key, value);
                }
            }
        }

        return separateCookies;
    }

    private void decodeSingleCookie(ConfigEntry configEntry, Properties properties, Map<String, String> cookies) {
        String nickKey = configEntry.getNickKey();
        String cookieValue = cookies.get(nickKey);

        String key = configEntry.getKey();
        String value = decodeValue(cookieValue, configEntry, properties); // value可能为null
        Attribute attribute = new Attribute(configEntry, properties, value);

        attributes.put(key, attribute);
    }

    private String decodeValue(String value, ConfigEntry configEntry, Properties properties) {
        if (StringUtils.isBlank(value)) {
            return value;
        }

        try {
            value = URLDecoder.decode(value, URL_ENCODING);
        } catch (Exception e) {
            //MonitorLog.addStat("TbSession", "CookieStore", "decodeVa解码失败", 0, 1);
            logger.error("解码失败", e);
            // 解码失败时直接返回，不继续解析
            return value;
        }

        if (configEntry.isEscapeJava()) {
            value = StringEscapeUtils.unescapeJava(value);
        } else if (configEntry.isEncrypt()) {
            value = BlowfishUtils.decryptBlowfish(value, getBlowfishKey(properties));
            if (configEntry.isBase64()) {
                value = Base64Utils.removeBase64Head(value);
            }
        } else if (configEntry.isBase64()) {
            value = Base64Utils.decodeBase64(value);
        }

        return value;
    }

    private static String getBlowfishKey(Properties properties) {
        String key = properties.getProperty(BLOWFISH_CIPHER_KEY);
        if (StringUtils.isNotBlank(key)) {
        	//temp solutions
        	if(StringUtils.indexOf(key, "=TAOBAO=") == -1 && !"taobao123".equals(key)){
        		key = BlowfishUtils.decryptBlowfish(key, "SEDe%&SDF*");
        	}
            return key;
        } else {
            throw new IllegalConfigException("必须指定" + BLOWFISH_CIPHER_KEY + "属性");
        }
    }

    public void setAttribute(ConfigEntry configEntry, Properties properties, Object value) {
        //MonitorLog.addStat("TbSession", "CookieStore", "setAttribute", 0, 1); // 哈勃
        String key = configEntry.getKey();
        // XXX 注意，这里调用了value上的toString()，而不是保存value对象本身
        String v = ObjectUtils.toString(value, null);
        Attribute attribute = new Attribute(configEntry, properties, v);
        attributes.put(key, attribute);
        dirty.add(key);
    }

    public void init(TaobaoSession session) {
        this.session = session;
        attributes = new HashMap<String, Attribute>();
        cookies = new HashMap<String, String>();
        dirty = new HashSet<String>();
        fetchCookies();
    }

    private void fetchCookies() {
        Cookie[] cookies = getRequest().getCookies();
        if (!ArrayUtils.isEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                this.cookies.put(cookie.getName(), cookie.getValue());
                // 设置wc2010 cookie到EnvSessionUtil
                if (EnvSessionUtil.ENV_PROJECT_NAME_COOKIE_KEY.equals(cookie.getName())) {
					EnvSessionUtil.setEnvProjectName(cookie.getValue());
                	//EnvSessionUtil.setEnvProjectName("daily");
				}
            }
        }
    }

    public void onSessionReady() {
        // empty
    }

    private TaobaoSessionServletRequest getRequest() {
        return session.getRequest();
    }

    private TaobaoSessionServletResponse getResponse() {
        return session.getResponse();
    }

    private SessionConfig getConfig() {
        return session.getConfig();
    }

    private int getClientVersion() {
        return session.getClientVersion();
    }

    private int getLatestVersion() {
        return session.getLatestVersion();
    }

    private Properties getClientVersionProperties() {
        return session.getClientVersionProperties();
    }

    // 以下方法只用于单元测试

    public Map<String, Attribute> getAttributes() {
        return attributes;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public Set<String> getDirty() {
        return dirty;
    }

    public TaobaoSession getSession() {
        return session;
    }
  public static void main (String args []){
	  System.out.print(BlowfishUtils.decryptBlowfish("taobao456", "SEDe%&SDF*"));
  }
}
