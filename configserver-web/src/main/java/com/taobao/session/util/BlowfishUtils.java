package com.taobao.session.util;

import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Blowfish�ӽ��ܵķ���
 *
 * @link http://www.schneier.com/blowfish.html
 * @author gus, hengyi
 */
public class BlowfishUtils {

    private static final Logger logger = Logger.getLogger(BlowfishUtils.class);

    private static final String CIPHER_NAME = "Blowfish/CFB8/NoPadding";

    private static final String KEY_SPEC_NAME = "Blowfish";

    private static final ThreadLocal<HashMap <String ,BlowfishUtils>> pool = new ThreadLocal<HashMap <String ,BlowfishUtils>>();
    
    private Cipher enCipher;

    private Cipher deCipher;

    private String key;

    private BlowfishUtils(String key) {
        try {
            this.key = key;

            String iv = StringUtils.substring(DigestUtils.md5Hex(key), 0, 8);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), KEY_SPEC_NAME);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
            enCipher = Cipher.getInstance(CIPHER_NAME);
            deCipher = Cipher.getInstance(CIPHER_NAME);
            enCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            deCipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        } catch (Exception e) {
            logger.error("��ʼ��BlowfishUtilsʧ��", e);
        }
    }

    public static String encryptBlowfish(String s, String key) {
        return getInstance(key).encrypt(s);
    }

    public static String decryptBlowfish(String s, String key) {
        return getInstance(key).decrypt(s);
    }

    private static BlowfishUtils getInstance(String key) {
       HashMap <String ,BlowfishUtils> keyMap = pool.get();
       if (null == keyMap){
    	   keyMap = new HashMap <String ,BlowfishUtils>();
    	   pool.set(keyMap);
       }
       BlowfishUtils instance = (BlowfishUtils) keyMap.get(key);
        if (instance == null || !StringUtils.equals(instance.key, key)) {
            instance = new BlowfishUtils(key);
            keyMap.put(key,instance);
        }
        return instance;
    }

    private void resetInstance() {
      pool.set(null);
    }

    /**
     * ���ܵķ���
     *
     * @param s
     * @return
     */
    private String encrypt(String s) {
        String result = null;

        if (StringUtils.isNotBlank(s)) {
            try {
                byte[] encrypted = enCipher.doFinal(s.getBytes());
                result = new String(Base64.encodeBase64(encrypted));
            } catch (Exception e) {
                resetInstance(); // ������ǰ���󣬷�ֹenCipher�����м�״̬
                logger.warn("����ʧ��", e);
            }
        }

        return result;
    }

    /**
     * ���ܵķ���
     *
     * @param s
     * @return
     */
    private String decrypt(String s) {
        String result = null;

        if (StringUtils.isNotBlank(s)) {
            try {
                byte[] decrypted = Base64.decodeBase64(s.getBytes());
                result = new String(deCipher.doFinal(decrypted));
            } catch (Exception e) {
                resetInstance(); // ������ǰ���󣬷�ֹdeCipher�����м�״̬
                logger.warn("����ʧ��", e);
            }
        }

        return result;
    }

}
