package com.taobao.session.util;

import java.io.IOException;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;

import com.alibaba.common.lang.StringUtil;
import com.taobao.session.SessionConfig;
import com.taobao.session.SessionKeyConstants;
import com.taobao.session.TaobaoSession;
import com.taobao.session.TaobaoSessionServletRequest;

public class UserCheckUtil {

    private static final Logger userTrackLog  = Logger.getLogger("userTrackLog");

    private static boolean      ipCheckSwitch = false;

    private static String       DOMAIN        = "domain";

    public static boolean isIpCheckSwitch() {
        return ipCheckSwitch;
    }

    public static void setIpCheckSwitch(boolean ipCheckSwitch) {
        UserCheckUtil.ipCheckSwitch = ipCheckSwitch;
    }

    /**
     * cookie 监控
     * @param request
     * @param response
     */
    @SuppressWarnings("unchecked")
    public static void doSessionList(TaobaoSession session) {
        try {
            StringBuffer clienetCookieList = new StringBuffer("<h1>Client</h1> :   </br>");
            Set<Entry<String, String>> cookieSet = TaobaoSession.getCookiesPool().entrySet();
           for(Entry entry: cookieSet) {
                clienetCookieList.append(entry.getKey() + "	" + entry.getValue() + "</br>");
            }
            session.getResponse().getWriter().write(clienetCookieList.toString());
            session.getResponse().commitBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户安全签名验证
     * 
     * @param session
     */
    public static void signatureCheck(TaobaoSession session) {
        String serverSg = (String) session.getAttribute(SessionKeyConstants.ATTRIBUTE_SIGNATURE);
        String signature = generatesecuritySignature(session);
        if (!StringUtil.equals(serverSg, signature)) {
            Cookie[] cookies = session.getRequest().getCookies();
            int cookieCount = cookies.length;
            int cookieSize = 0;
            for (Cookie cookie : cookies) {
                cookieSize += cookie.getName().getBytes().length + cookie.getValue().getBytes().length + 1;
            }
            userTrackLog.error("用户安全签名不匹配: nick:"
                    + session.getAttribute(SessionKeyConstants.ATTRIBUTE_NICK) + " uidn:"
                    + session.getAttribute(SessionKeyConstants.ATTRIBUTE_USER_ID_NUM) + " uid:"
                    + session.getAttribute(SessionKeyConstants.ATTRIBUTE_USER_ID) + "--signature："
                    + session.getAttribute(SessionKeyConstants.ATTRIBUTE_SIGNATURE)
                    + "--cookieCount: " + cookieCount + "--cookieSize: " + cookieSize);
            session.invalidate();
        }
    }

    /**
     * 生产用户安全签名
     * 
     * @param session
     * @return
     */
    public static String generatesecuritySignature(TaobaoSession session) {
        String nick = (String) session.getAttribute(SessionKeyConstants.ATTRIBUTE_NICK);
        String userIdNum = (String) session.getAttribute(SessionKeyConstants.ATTRIBUTE_USER_ID_NUM);
        String userID = (String) session.getAttribute(SessionKeyConstants.ATTRIBUTE_USER_ID);
        if (StringUtil.isNotBlank(userIdNum) && StringUtil.isNotBlank(userID)
                && StringUtil.isNotBlank(nick)) {
            char uid = userID.charAt(userID.length() - 1);
            char uin = userIdNum.charAt(userIdNum.length() - 1);
            char n = nick.charAt(nick.length() - 1);
            String signature = new StringBuffer().append(n).append(uin).append(uid).toString();
            return signature;
        }
        return null;

    }

    public static String generateSessionIP(TaobaoSession session) {
        String[] ipStr = session.getRequest().getRemoteAddr().split("\\.");
        String clientip = Integer.toHexString(Integer.parseInt(ipStr[0])) + Integer.toHexString(Integer.parseInt(ipStr[1]));
        return clientip;
    }

    public static boolean domainCheck(TaobaoSessionServletRequest request, SessionConfig config) {
        String url = request.getRequestURL().toString();
        Pattern p = Pattern.compile("(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)",Pattern.CASE_INSENSITIVE);
        try {
            Matcher matcher = p.matcher(url);
            matcher.find();
            String requestDomain = matcher.group();
            String defaultDomain = config.getDefaultConfig().getProperty(DOMAIN);
            if (defaultDomain.indexOf(requestDomain) != -1) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
