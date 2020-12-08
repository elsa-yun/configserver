package com.taobao.session;

/**
 * 参见taobao-session.xml
 * @author dilong
 */
public interface SessionKeyConstants {
	String ATTRIBUTE_SIGNATURE = "signature";
    String ATTRIBUTE_USER_ID = "userID";
    String ATTRIBUTE_SESSION_ID = "sessionID";
    String ATTRIBUTE_TARGETURL = "targetURL";
    String ATTRIBUTE_NICK = "_nk_";
    String ATTRIBUTE_NUM_OF_MSG = "_msg_";
    String ATTRIBUTE_MOBILE_GOT_PASSWORD = "_mgp_";//标识是否已经通过手机校验，修改密码
    String ATTRIBUTE_MODIFY_MOBILE = "_mm_";//标识是否可以修改手机
    String ATTRIBUTE_SERVICE_TYPE = "service_type";
    String ATTRIBUTE_VOICE_OF_MSG = "_msg_voice";//站内信是否提示声音
    String ATTRIBUTE_AUTH_MOVE = "_auth_am_";
    String ATTRIBUTE_AUTH_DEL = "_auth_ad_";
    String ATTRIBUTE_GSN = "GSN";
    String ATTRIBUTE_VISITED = "ISVISITED";
    String ATTRIBUTE_TRACE_DATA = "_trace_data_";
    String ATTRIBUTE_CC = "_cc_";
    String ATTRIBUTE_LAST_VISIT_COOKIE = "lastVisitCookie";
    String ATTRIBUTE_LOGIN = "login";
    String ATTRIBUTE_SESS_START = "sessionStartTime";
    String ATTRIBUTE_IP = "ipAddress";
    String ATTRIBUTE_LAST_VISIT_DB = "lastVisitDB";
    String ATTRIBUTE_CHECK_CODE = "checkCode";
    String ATTRIBUTE_COOKIE_CHECK_CODE = "cookieCheckCode";
    String ATTRIBUTE_USER_ID_NUM = "userIDNum";
    String ATTRIBUTE_ROLE = "role";
    String ATTRIBUTE_COOKIE_OK = "cookieEnabled";
    String ATTRIBUTE_AUCTION_DATA = "wwwtaobaocom_auction_data";
    String ATTRIBUTE_EXIST_SHOP = "existShop";
    String ATTRIBUTE_EXIST_XSHOP = "existXShop";
    String ATTRIBUTE_PUBLISH_ITEM = "publishItemObj";
    String ATTRIBUTE_PUBLISH_OBJ = "publishObj";
    String ATTRIBUTE_USER_FROM = "userFrom";
    String ATTRIBUTE_ALLYES_AD_COOKIE = "_ad_";
    String ATTRIBUTE_NUM_OF_WWMSG = "_wwmsg_";
    String ATTRIBUTE_LAST_GET_WWMSG = "lastgetwwmsg";
    String ATTRIBUTE_PROMOTED_TYPE = "promoted_type";
    String ATTRIBUTE_TRACK_NICK = "tracknick";
    String ATTRIBUTE_UNACT = "unact";
    String ATTRIBUTE_AD_EMAIL = "ademail";
    String ATTRIBUTE_BUY_ITEM_ID = "buyitemid";
    String ATTRIBUTE_SIP = "sip";
    String ATTRIBUTE_SSLLOGIN = "ssllogin";
    String ATTRIBUTE_REG_LIST = "reglist";
    String ATTRIBUTE_REG_TIME = "regtime"; //会员注册时间[秒数] afei on 20070827
    String ATTRIBUTE_SHOW_STYLE = "_show_";
    String ATTRIBUTE_IS_BUYER = "_isbuyer_";
    String ATTRIBUTE_OC = "_oc_";
    String ATTRIBUTE_TRACK_ID = "trackid";
    String ATTRIBUTE_LAST_LOGIN_TIME = "lastLoginTime";
    String ATTRIBUTE_IS_GUEST_LOGIN = "is_guest_login";
    String ATTRIBUTE_NEW_TRACE = "newtrace";
    String ATTRIBUTE_ALLYES_CHSESS = "allyes_chsess";
    String ATTRIBUTE_TG = "tg"; //推广页面标志
    String ATTRIBUTE_YAHOO_BINDING = "yahoobinding"; //是否绑定雅虎ID[true|false] afei on 20070827
    String ATTRIBUTE_YAHOO_YPID = "ypid"; // 登录时存进YPID qingming.wang 2007.11.26
    String ATTRIBUTE_YAHOO_YMC = "ymc";   // 上次新到邮件数 qingming.wang 2007.12.17
    
	/**	一下是配置文件里有login和detail的 SessionKeeper里却没有的，暂缓取名
		"_l_g_"
		"_lang"
		"_reg_encrypt_table_"
		"_reg_rand_name_"
		"_reg_time_check_"
		"_tb_token_"
		"_web_flow_state_"
		"action_type"
		"batchbuy"
		"btc"
		"caid"
		"cart_items"
		"cart_merge"
		"cart_price"
		"dynamic_login"
		"enabledWuiShop"
		"expressway_game"
		"expressway_life"
		"expressway_mobile"
		"expressway_search"
		"isWuiShop"
		"miid"
		"ncount"
		"ssl_dynamic_login"
		"topDomain"
		"topLogin"
		"zfbcertificateduser"
	 */
}
