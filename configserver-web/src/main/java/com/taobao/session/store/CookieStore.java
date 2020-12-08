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
     * ���ڱ�Ǳ���ʧ��
     */
    private static final String ERROR = new String("ERROR");

    /**
     * ����ѽ���������
     */
    private Map<String, Attribute> attributes;

    /**
     * ���δ����������
     */
    private Map<String, String> cookies;

    /**
     * ����޸Ĺ�������
     */
    private Set<String> dirty;

    private TaobaoSession session;

    public void commit() {
        String[] originalDirty = dirty.toArray(new String[dirty.size()]);
        for (String key : originalDirty) {
            if (dirty.contains(key)) { // ��key�����Ѿ���֮ǰ�����cookie�д�����������Ҫ�ȼ���Ƿ���dirty��
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
        	logger.warn("configGroup��Ӧ��Ϊ�գ�����" + config.getClass().getName() + "��ʵ��");
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
            Object attribute = getAttribute(thisConfigEntry); // Ϊnullʱ��ʾ�������ѱ�ɾ��
            String value = attribute != null ? attribute.toString() : null;
            value = encodeValue(value, thisConfigEntry, properties);
            // ��ֹ��α���cookie
            removeFromDirty(thisConfigEntry);
            if (value == null || value == ERROR) { // �������ѱ�ɾ����������ʱ���Ե�ǰ����
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
        Object attribute = getAttribute(configEntry); // Ϊnullʱ��ʾ�������ѱ�ɾ��
        String value = attribute != null ? attribute.toString() : StringUtils.EMPTY;
        value = encodeValue(value, configEntry, properties);
        if (value == ERROR) { // �������ʱ���Ե�ǰcookie
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
            // XXX ���Ǳ��룬��ԭʵ�ֲ�ͬ��ԭʵ�������cookieʱֻ�Լ��ܵ����Ա���
            value = URLEncoder.encode(value, URL_ENCODING);
        } catch (Exception e) {
            logger.error("����ʧ��", e);
            //MonitorLog.addStat("TbSession", "CookieStore", "singleCookie����ʧ��", 0, 1);
            // ����ʧ��ʱ�����ش����ǣ��Ҳ����浽cookies��
            return ERROR;
        }
        return value;
    }

    private void addCookieToResponse(ConfigEntry configEntry, String name, String value, boolean removed) {
        // ֻ�h���Ѵ��ڵ�cookie
        if (removed && !cookies.containsKey(name)) {
            return;
        }

        String domain = configEntry.getDomain();
        // XXX ɾ��cookieʱ��maxAge��Ϊ0����ԭ����ʵ��ֻ�ǽ�ֵ��Ϊ�ն���
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

        if (attribute == null) { // ����δ������
            decodeCookie(configEntry, properties);
            // �ٴβ�������
            attribute = attributes.get(key);
        }

        // XXX valueΪnullʱҪ��Ϊ�մ�(���ʵ�ּ���?)

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
        Collection<ConfigEntry> configGroup = config.getConfigGroup(compressKey, getClientVersion()); // ���ͻ��˵�ǰ�汾����
        if (configGroup != null && configGroup.size() > 0) {
            for (ConfigEntry thisConfigEntry : configGroup) {
                if (!dirty.contains(thisConfigEntry.getKey())) { // ���������û�б�д������Ž��н��룬����ԭ��д���ֵ�ᱻ����
                    decodeSingleCookie(thisConfigEntry, properties, separateCookies);
                }
            }
        } else {
            logger.warn("configGroup��Ӧ��Ϊ�գ�����" + config.getClass().getName() + "��ʵ��");
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
        String value = decodeValue(cookieValue, configEntry, properties); // value����Ϊnull
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
            //MonitorLog.addStat("TbSession", "CookieStore", "decodeVa����ʧ��", 0, 1);
            logger.error("����ʧ��", e);
            // ����ʧ��ʱֱ�ӷ��أ�����������
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
            throw new IllegalConfigException("����ָ��" + BLOWFISH_CIPHER_KEY + "����");
        }
    }

    public void setAttribute(ConfigEntry configEntry, Properties properties, Object value) {
        //MonitorLog.addStat("TbSession", "CookieStore", "setAttribute", 0, 1); // ����
        String key = configEntry.getKey();
        // XXX ע�⣬���������value�ϵ�toString()�������Ǳ���value������
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
                // ����wc2010 cookie��EnvSessionUtil
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

    // ���·���ֻ���ڵ�Ԫ����

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
