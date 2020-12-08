package com.taobao.session;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.common.lang.StringUtil;
import com.taobao.session.config.ConfigServerXmlConfig;
import com.taobao.session.config.SpringConfig;
import com.taobao.session.store.CookieStore;
import com.taobao.session.store.TairStore;
import com.taobao.session.util.ClassUtils;
import com.taobao.session.util.ConfigUtils;
import com.taobao.session.util.UserCheckUtil;

/**
 * @author huangshang, hengyi
 */
public class TaobaoSessionFilter implements Filter {

    @SuppressWarnings("unchecked")
    private static final Class<? extends SessionStore>[] DEFAULT_STORE_CLASSES = new Class[]{CookieStore.class};

    private static final String SESSION_CONFIG_CLASS = "sessionConfigClass";

    public static final String GROUP_PARAM_NAME = "tbsessionConfigGroup";

    public static final String IS_LOGIN_SESSIONCHECK ="isloginSessionCheck";

    public static final String IS_HIGH_VISIT_APP ="isHighVistApp";

    public static final String NEED_SG_CHECK ="needSgCheck";

    private static final String ADDITIONAL_STORE_CLASSES = "additionalStoreClasses";

    private static final String ALLLOW_FORBIDDEN_COOKIE = "alllowForbiddenCookie";

    private static final String SESSION_ID_NICK_KEY = "cookie2";

    private static final String SESSION_ID_STORE_KEY = "cookie";

    private static final int SESSION_ID_LIFE_CYCLE = -1;

    private static final String VERSION_NICK_KEY = "v";

    private static final String VERSION_STORE_KEY = "cookie";

    private static final int VERSION_LIFE_CYCLE = Integer.MAX_VALUE;

    private static final String STORE_POSTFIX = "Store";

    private static final char STANDARD_NAME_SEPARATOR = '-';

    private SessionConfig sessionConfig;

    private SessionStoreFactory sessionStoreFactory;

    private FilterConfig filterConfig;

    private boolean  sesionConfigIsNew = false; ;

    private boolean  useLoginCheck = false ;

    private boolean  needSgChecked = true ;

    private boolean  isHighVistApp = false;

    private static final Logger logger = Logger.getLogger(TaobaoSessionFilter.class);

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // ���������filter������exception��
        // ��weblogic�У�servlet forward��jspʱ��jsp�Ի���ô�filter����jsp�׳����쳣�ͻᱻ��filter����
        if (!(request instanceof HttpServletRequest && response instanceof HttpServletResponse) || (request.getAttribute(getClass().getName()) != null)) {
            chain.doFilter(request, response);
            return;
        }
        // ��ֹ����.
        request.setAttribute(getClass().getName(), Boolean.TRUE);
        TaobaoSessionServletRequest tbRequest = new TaobaoSessionServletRequest((HttpServletRequest) request);
        TaobaoSessionServletResponse tbResponse = new TaobaoSessionServletResponse((HttpServletResponse) response);
        TaobaoSession session = createTaobaoSession(tbRequest, tbResponse);
        tbRequest.setSession(session);
        tbResponse.setSession(session);
        //�ռ������û�о������õ�cookie
        session.cookieCollect();
        if (tbRequest.getRequestURI().endsWith("viewCookie.jsp")) {
            if (StringUtil.equals(tbRequest.getParameter("clear"), "true")) {
                TaobaoSession.getCookiesPool().clear();
                return;
            }
            UserCheckUtil.doSessionList(session);
            return;
        }
        //Response���濪�ء�
        if (filterConfig.getInitParameter("needResponseBuffered") == "false") {
            tbResponse.setWriterBuffered(false);
        }
        //cookie��Ч�ڼ��
        if (useLoginCheck) {
            useDefaultIslogin(session);
        }
        boolean isDomianEqual = UserCheckUtil.domainCheck(tbRequest, sessionConfig);
        //tair��Ч�ڼ��
        if (sesionConfigIsNew && (!isHighVistApp || !isDomianEqual)) {
            updateTairExpired(session);
        }
        // ����һ�²����а�ȫǩ��У��
        if (needSgChecked && isDomianEqual) {
            UserCheckUtil.signatureCheck(session);
        }
        try {
            chain.doFilter(tbRequest, tbResponse);
            if (null != session) {
                session.commit();
            }
        } finally {
            tbResponse.commitBuffer();
        }
    }

    /**
     * 15���Ӹ���һ��tair��
     * @param session
     */
    private void updateTairExpired(TaobaoSession session) {
        try {
            if (isLogin(session)) {
                session.setExpiredTimeChanged(false);
                String lastTime = (String) session.getAttribute("tairlastUpdatetime");
                long tairlastUpdatetime = 0;
                if (StringUtils.isNotBlank(lastTime)) {
                    try {
                        tairlastUpdatetime = Integer.parseInt(lastTime);
                    } catch (NumberFormatException e1) {
                        tairlastUpdatetime = 0;
                    }
                    if ((System.currentTimeMillis() / 1000 - tairlastUpdatetime) > 900) {
                        session.setExpiredTimeChanged(true);
                        session.setAttribute("tairlastUpdatetime", Long.toString(System.currentTimeMillis() / 1000));
                    }
                } else {
                    session.setAttribute("tairlastUpdatetime", Long.toString(System.currentTimeMillis() / 1000));
                }
            }
        } catch (Exception ex) {
            logger.error("TaobaoSessionFilter: exception update TariExipred!!!", ex);
        }
    }


    /*
    * cookie��Ч�ڼ���͸���
    */
    private void useDefaultIslogin(TaobaoSession session) {
        try {
            if (isLogin(session)) {
                String lastTime = (String) session.getAttribute("lastVisitCookie");
                int lastVisitTime = 0;
                if (lastTime != null) {
                    try {
                        lastVisitTime = Integer.parseInt(lastTime);
                    } catch (NumberFormatException e1) {
                        lastVisitTime = 0;
                    }
                }
                if ((!sesionConfigIsNew) && ((System.currentTimeMillis() / 1000 - lastVisitTime) >= 3600)) {
                    session.invalidate();
                }
            }
            // �����ϴη���ʱ��
            session.setAttribute("lastVisitCookie", Long.toString(System.currentTimeMillis() / 1000));
        } catch (Exception ex) {
            logger.error("TaobaoSessionFilter: exception while saving session data!!!", ex);
        }
    }


    private boolean isLogin(HttpSession tbSess) {
        boolean login = false;
        if (tbSess == null) {
            return login;
        }
        if ("true".equals(tbSess.getAttribute("login"))) {
            login = true;
        }
        return login;
    }

    private TaobaoSession createTaobaoSession(TaobaoSessionServletRequest tbRequest, TaobaoSessionServletResponse tbResponse) {
        TaobaoSession session = null;
        try {
            // ��ʼ��session
            Map<String, SessionStore> storeMap = sessionStoreFactory.createStoreMap();
            session = new TaobaoSession(tbRequest, tbResponse, filterConfig.getServletContext(), sessionConfig, storeMap,isHighVistApp);
            session.init();
            session.setAlllowForbiddenCookie(Boolean.parseBoolean(this.filterConfig.getInitParameter(ALLLOW_FORBIDDEN_COOKIE)));
        } catch (Exception e) {
            logger.error("����session����, ��ٵ�ǰsession", e);
            session = null;
        }
        return session;
    }

    public void init(FilterConfig filterConfig) throws ServletException {
         this.filterConfig = filterConfig;
         String  defaultLoginCheck   = filterConfig.getInitParameter(IS_LOGIN_SESSIONCHECK);
         String  tbsessionConfigserverGroup = filterConfig.getInitParameter(GROUP_PARAM_NAME);
         String  HighVistApp = filterConfig.getInitParameter(IS_HIGH_VISIT_APP);
         String  needSgCheck = filterConfig.getInitParameter(NEED_SG_CHECK);
         if ( StringUtil.equals("true" ,defaultLoginCheck)){
              useLoginCheck = true;
         }

         if ( StringUtil.equals("false" ,needSgCheck)){
             needSgChecked = false;
        }

         if ( StringUtil.equals("true" ,HighVistApp)){
             isHighVistApp = true;
         }

         if (tbsessionConfigserverGroup.indexOf("new") >=0) {
              sesionConfigIsNew = true ;
         }

        try {
            initSessionConfig();
            initSessionStoreFactory();
        } catch (Exception e) {
            logger.error("taobao session init failed, System exit,Please check the configuration,especialy the configServer config.", e);
            System.exit(0);
        }
    }

    /**
     * ��������϶��ǲ����� sessionConfigClass�ģ�Ĭ���� ConfigServerConfig ���config
     * server������
     */
    private void initSessionConfig() throws ServletException {
        String className = filterConfig.getInitParameter(SESSION_CONFIG_CLASS);
        Class<? extends SessionConfig> configClass = null;
        if (StringUtils.isBlank(className)) {
//            configClass = ConfigServerXmlConfig.class; // Ĭ��SessionConfigʵ��
//        } else {
//            configClass = ClassUtils.findClass(className, SessionConfig.class);
        }
        configClass = SpringConfig.class;
        sessionConfig = ClassUtils.newInstance(configClass);
        addSessionIDConfigEntryToSessionConfig();
        addVersionConfigEntryToSessionConfig();
        sessionConfig.init(filterConfig);
    }

    private void addVersionConfigEntryToSessionConfig() {
        addReadOnlyConfigEntryToSessionConfig(TaobaoSession.VERSION, VERSION_NICK_KEY, VERSION_STORE_KEY,
                VERSION_LIFE_CYCLE);
    }

    private void addSessionIDConfigEntryToSessionConfig() {
        addReadOnlyConfigEntryToSessionConfig(TaobaoSession.SESSION_ID, SESSION_ID_NICK_KEY, SESSION_ID_STORE_KEY,
                SESSION_ID_LIFE_CYCLE);
    }

    private void addReadOnlyConfigEntryToSessionConfig(String key, String nickKey, String storeKey, int lifeCycle) {
        ConfigEntry configEntry = new ConfigEntry();
        configEntry.setKey(key);
        configEntry.setNickKey(nickKey);
        configEntry.setStoreKey(storeKey);
        configEntry.setReadOnly(true);
        configEntry.setLifeCycle(lifeCycle);
        sessionConfig.addConfigEntry(configEntry);
    }

    private void initSessionStoreFactory() {
        Map<String, Class<? extends SessionStore>> storeTypeMap = new HashMap<String, Class<? extends SessionStore>>();
        addDefaultStoreClassesToStoreTypeMap(storeTypeMap);
        addAdditionalStoreClassesToStoreTypeMap(storeTypeMap);

        sessionStoreFactory = new SessionStoreFactory();
        sessionStoreFactory.setStoreTypeMap(storeTypeMap);
    }

    private void addAdditionalStoreClassesToStoreTypeMap(Map<String, Class<? extends SessionStore>> storeTypeMap) {
        String param = filterConfig.getInitParameter(ADDITIONAL_STORE_CLASSES);
        String[] additionalStoreClassNames = ConfigUtils.splitConfig(param);
        if (!ArrayUtils.isEmpty(additionalStoreClassNames)) {
            for (String className : additionalStoreClassNames) {
                Class<? extends SessionStore> storeClass = ClassUtils.findClass(className, SessionStore.class);
                addToStoreTypeMap(storeClass, storeTypeMap);
            }
        }
    }

    private void addDefaultStoreClassesToStoreTypeMap(Map<String, Class<? extends SessionStore>> storeTypeMap) {
        for (Class<? extends SessionStore> storeClass : DEFAULT_STORE_CLASSES) {
            addToStoreTypeMap(storeClass, storeTypeMap);
        }
    }

    private void addToStoreTypeMap(Class<? extends SessionStore> storeClass, Map<String, Class<? extends SessionStore>> storeTypeMap) {
        String className = storeClass.getSimpleName();
        String standardName = convertClassNameToStandardName(className, STORE_POSTFIX);
        storeTypeMap.put(standardName, storeClass);
    }

    /**
     * ����ʽ��"AbcDefStore"������(����postfixΪ"Store")��ת��Ϊ��ʽ��"abc-def"�ı�׼���
     */
    private String convertClassNameToStandardName(String className, String postfix) {
        if (StringUtils.endsWith(className, postfix)) {
            className = StringUtils.substringBeforeLast(className, postfix);
        }
        String[] words = StringUtils.splitByCharacterTypeCamelCase(className);
        String joinedWords = StringUtils.join(words, STANDARD_NAME_SEPARATOR);
        String standardName = StringUtils.lowerCase(joinedWords);
        return standardName;
    }

    public void destroy() {
        // empty
    }

    // ���·���ֻ���ڵ�Ԫ����

    public SessionConfig getSessionConfig() {
        return sessionConfig;
    }

    public void setSessionConfig(SessionConfig sessionConfig) {
        this.sessionConfig = sessionConfig;
    }

    public SessionStoreFactory getSessionStoreFactory() {
        return sessionStoreFactory;
    }

    public void setSessionStoreFactory(SessionStoreFactory sessionStoreFactory) {
        this.sessionStoreFactory = sessionStoreFactory;
    }

    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }
public static void main(String[] args) {
       String className = CookieStore.class.getSimpleName();
       System.out.println(className);
       TaobaoSessionFilter filter = new TaobaoSessionFilter();
       System.out.println(filter.convertClassNameToStandardName(className, STORE_POSTFIX));
}
}