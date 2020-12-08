package com.taobao.session.util;

import java.security.SecureRandom;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author hengyi
 */
public class Base64Utils {

    private static final String FIXED_BASE64_HEAD = "a#b$^";

    private static final Logger logger = Logger.getLogger(Base64Utils.class);

    private static final Random random = new SecureRandom();

    public static String encodeBase64(String s) {
        String result = null;

        if (StringUtils.isNotBlank(s)) {
            try {
                byte[] encoded = Base64.encodeBase64(s.getBytes());
                result = new String(encoded);
            } catch (Exception e) {
                logger.warn("Base64�������", e);
            }
        }

        return result;
    }

    public static String decodeBase64(String s) {
        String result = null;

        if (StringUtils.isNotBlank(s)) {
            try {
                byte[] decoded = Base64.decodeBase64(s.getBytes());
                result = new String(decoded);
            } catch (Exception e) {
                logger.warn("Base64�������", e);
            }
        }

        return result;
    }

    public static String addBase64Head(String s) {
        return random.nextInt(10) + FIXED_BASE64_HEAD + s;
    }

    public static String removeBase64Head(String s) {
        if (s != null && s.length() > FIXED_BASE64_HEAD.length() + 1) {
            return s.substring(FIXED_BASE64_HEAD.length() + 1);
        } else {
            return s;
        }
    }

}
