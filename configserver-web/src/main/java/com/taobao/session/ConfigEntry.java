package com.taobao.session;

import org.apache.commons.lang.StringUtils;

/**
 * @author hengyi
 */
public class ConfigEntry {

    // 为了保证在多线程下的可见性，所有字段都必须标记为volatile

    /**
     * 程序中使用的属性的名字，对应于HttpSession.getAttribute()方法的参数
     */
    private volatile String key;

    /**
     * 保存时使用的名字，譬如cookie的名字
     */
    private volatile String nickKey;

    /**
     * 使用的存储类型，如"cookie"、"tair"
     */
    private volatile String storeKey;

    /**
     * 是否需要加密
     */
    private volatile boolean encrypt;

    /**
     * 是否做base64编码
     */
    private volatile boolean base64;

    /**
     * 是否对特殊字符进行转义
     */
    private volatile boolean escapeJava;

    /**
     * 是否需要组合压缩
     */
    private volatile boolean compress;

    /**
     * 组合压缩后的key
     */
    private volatile String compressKey;

    /**
     * 存储的生命周期，即失效时间
     */
    private volatile int lifeCycle = -1;

    /**
     * cookie的域名
     */
    private volatile String domain;

    /**
     * cookie的路径
     */
    private volatile String cookiePath = "/";

    /**
     * cookie的httpOnly属性
     */
    private volatile boolean httpOnly;

    /**
     * 是否是只读属性，一般只用于sessionID
     */
    private volatile boolean readOnly;

    /**
     * 配置项的版本号
     */
    private volatile int version;

    /**
     * 是否已被删除
     */
    private volatile boolean removed;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNickKey() {
        return nickKey;
    }

    public void setNickKey(String nickKey) {
        this.nickKey = nickKey;
    }

    public String getStoreKey() {
        return storeKey;
    }

    public void setStoreKey(String storeKey) {
        this.storeKey = storeKey;
    }

    public int getLifeCycle() {
        return lifeCycle;
    }

    public void setLifeCycle(int lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    public boolean isEncrypt() {
        return encrypt;
    }

    public void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }

    public boolean isBase64() {
        return base64;
    }

    public void setBase64(boolean base64) {
        this.base64 = base64;
    }

    public void setEscapeJava(boolean escapeJava) {
        this.escapeJava = escapeJava;
    }

    public boolean isEscapeJava() {
        return escapeJava;
    }

    public boolean isCompress() {
        return compress;
    }

    private void setCompress(boolean compress) {
        this.compress = compress;
    }

    public String getCompressKey() {
        return compressKey;
    }

    public void setCompressKey(String compressKey) {
        this.compressKey = compressKey;
        setCompress(StringUtils.isNotBlank(compressKey));
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getCookiePath() {
        return cookiePath;
    }

    public void setCookiePath(String cookiePath) {
        this.cookiePath = cookiePath;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (base64 ? 1231 : 1237);
        result = prime * result + (compress ? 1231 : 1237);
        result = prime * result + ((compressKey == null) ? 0 : compressKey.hashCode());
        result = prime * result + ((cookiePath == null) ? 0 : cookiePath.hashCode());
        result = prime * result + ((domain == null) ? 0 : domain.hashCode());
        result = prime * result + (encrypt ? 1231 : 1237);
        result = prime * result + (escapeJava ? 1231 : 1237);
        result = prime * result + (httpOnly ? 1231 : 1237);
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + lifeCycle;
        result = prime * result + ((nickKey == null) ? 0 : nickKey.hashCode());
        result = prime * result + (readOnly ? 1231 : 1237);
        result = prime * result + (removed ? 1231 : 1237);
        result = prime * result + ((storeKey == null) ? 0 : storeKey.hashCode());
        result = prime * result + version;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ConfigEntry other = (ConfigEntry) obj;
        if (base64 != other.base64)
            return false;
        if (compress != other.compress)
            return false;
        if (compressKey == null) {
            if (other.compressKey != null)
                return false;
        } else if (!compressKey.equals(other.compressKey))
            return false;
        if (cookiePath == null) {
            if (other.cookiePath != null)
                return false;
        } else if (!cookiePath.equals(other.cookiePath))
            return false;
        if (domain == null) {
            if (other.domain != null)
                return false;
        } else if (!domain.equals(other.domain))
            return false;
        if (encrypt != other.encrypt)
            return false;
        if (escapeJava != other.escapeJava)
            return false;
        if (httpOnly != other.httpOnly)
            return false;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        if (lifeCycle != other.lifeCycle)
            return false;
        if (nickKey == null) {
            if (other.nickKey != null)
                return false;
        } else if (!nickKey.equals(other.nickKey))
            return false;
        if (readOnly != other.readOnly)
            return false;
        if (removed != other.removed)
            return false;
        if (storeKey == null) {
            if (other.storeKey != null)
                return false;
        } else if (!storeKey.equals(other.storeKey))
            return false;
        if (version != other.version)
            return false;
        return true;
    }

    /**
     * Constructs a <code>String</code> with all attributes
     * in name = value format.
     *
     * @return a <code>String</code> representation
     * of this object.
     */
    public String toString()
    {
        final String TAB = ", ";

        String retValue = "";

        retValue = "ConfigEntry("
            + super.toString() + TAB
            + "key = " + this.key + TAB
            + "nickKey = " + this.nickKey + TAB
            + "storeKey = " + this.storeKey + TAB
            + "encrypt = " + this.encrypt + TAB
            + "base64 = " + this.base64 + TAB
            + "escapeJava = " + this.escapeJava + TAB
            + "compress = " + this.compress + TAB
            + "compressKey = " + this.compressKey + TAB
            + "lifeCycle = " + this.lifeCycle + TAB
            + "domain = " + this.domain + TAB
            + "cookiePath = " + this.cookiePath + TAB
            + "httpOnly = " + this.httpOnly + TAB
            + "readOnly = " + this.readOnly + TAB
            + "version = " + this.version + TAB
            + "removed = " + this.removed + TAB
            + ")";

        return retValue;
    }

}
