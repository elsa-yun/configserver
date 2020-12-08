package com.taobao.session.store;

import static com.taobao.session.util.ConfigUtils.getPropertiesVersion;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.common.lang.StringUtil;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.taobao.session.ConfigEntry;
import com.taobao.session.IllegalConfigException;
import com.taobao.session.SessionStore;
import com.taobao.session.TaobaoSession;
import com.taobao.session.config.ServerSessionList;
//import com.taobao.tair.DataEntry;
//import com.taobao.tair.Result;
//import com.taobao.tair.ResultCode;
//import com.taobao.tair.TairManager;

/**
 * @author hengyi
 */
public class TairStore implements SessionStore {


//    private static final Logger logger = Logger.getLogger(TairStore.class);

    private static final String TAIR_RESET_ERROR_COUNT = "tair.resetErrorCount";

    private static final String TAIR_NAMESPACE = "tair.namespace";

    private static final String TAIR_GROUP_NAME = "tair.groupName";

    private static final String TAIR_CONFIG_SERVERS = "tair.configServers";

    private static  long  lastReadTairTime =0L ;
    /**
     * ���ֿ��ǣ�����Tair��Ⱥ��Ҫͬ��
     * �����������tair�Լ���Ⱥ���֣�session�в�����
     */
//    private static final String TAIR_NAMESPACE_2 = "tair2.namespace";

//    private static final String TAIR_GROUP_NAME_2 = "tair2.groupName";

//    private static final String TAIR_CONFIG_SERVERS_2 = "tair2.configServers";

//    private static final String FIRST_TAIR_NAME = "Tair";

//    private static final int FIRST_TAIR = 1;

    private static final int ERROR_COUNT_LIMIT = 1000;

    /**
     * ���̳߳�����д�뱸�ݼ�Ⱥ�����̳߳ز����ƴ�С����Ϊһ��д��������һ�������̴߳�������������ĸ�����
     * jboss����������߳�����ͬ�������̳߳ز�����������
     */
  //   private static final ExecutorService writeToSecondTairThreadPool = Executors.newCachedThreadPool();

    private static final AtomicInteger configVersion = new AtomicInteger(0);

    private static  AtomicInteger errorCount = new AtomicInteger(0);

    /**
     * �����������߳�(һ���������̣߳���һ�����̳߳��е��߳�)ͬʱ�����������ԣ�������Щ���Զ���Ҫ����volatile����
     */
    private volatile Map<String, Object> attributes;

//	/private volatile int tair;

	private volatile int version = 0;

	private volatile TaobaoSession session;

	/*
	 * tair ��������ݸ��±�־λ
	 */
	private volatile boolean tairDataChanged = false;

	/*
	 * tair����ʱ����±�־λ
	 */
	private volatile boolean expiredTimeChanged = false;

	/*
	 * �Ƿ��й���tair������־
	 */
	private volatile boolean  aleadayReaded=false;



	public void init(TaobaoSession session) {
		this.session	=	session;
		expiredTimeChanged	=	false;
		this.tairDataChanged	 =	false;
		this.aleadayReaded	=	false;
		attributes	=	new HashMap<String, Object>();
//		tair = 1;
		version = 0;
	}

	  /*
     *  ������µ��������͹���������Ƿ�����Tair��дʧ�ܼ��������㡣
     */
    private void checkAndResetErrorCount(Properties properties) {
        int newVersion = getPropertiesVersion(properties);
        int oldVersion = configVersion.getAndSet(newVersion);
        if (oldVersion != newVersion) { // �汾������
            // ��Ⱥ����ָ�������һ�����ô�������
            String reset = properties.getProperty(TAIR_RESET_ERROR_COUNT);
            if (!StringUtils.isBlank(reset)) {
                 errorCount.set(0);
                 ServerSessionList.addInfo("������errorCount��tair��Ⱥ�ָ�");
            }
        }
    }

    public Object getAttribute(ConfigEntry configEntry, Properties properties){
        if (StringUtil.isNotEmpty(session.getId())){
           if (null == attributes || aleadayReaded == false) {
				aleadayReaded = readFromTair();
		}
        }
		String key = configEntry.getKey();
        return attributes.get(key);
    }

    public void setAttribute(ConfigEntry configEntry, Properties properties, Object value) {
        if (StringUtil.isNotEmpty(session.getId())){
            if (null == attributes || aleadayReaded == false) {
            aleadayReaded = readFromTair();
            if (aleadayReaded == false)
                return;
        }
        String key = configEntry.getKey();
        if (value != null) {
            // XXX ע�⣬���������value�ϵ�toString()�������Ǳ���value������
            String v = ObjectUtils.toString(value, null);
            attributes.put(key, v);
        } else {
            attributes.remove(key);
        }
        this.setTairDataChanged(true);
        }

    }
	/**
	 *
	 * @category  session��Ϣд��Tair��д��ʧ�ܼ�����++ ��д��ɹ�������--
	 **/
    private void writeToTair() {
    	try{
    		final Properties properties = getLatestVersionProperties();
    		// ֻ��д��ʱ����ÿ���������ʱ���reset�����ڶ�ȡʱ��飬���ټ�����
    		checkAndResetErrorCount(properties);
    		boolean firstTairDown = isFirstTairDown();
    		 // �ж�Tair��Ⱥ�Ƿ��ѹ�
    		if (!firstTairDown) {
    			boolean isTairWriteSucess = writeToTair(properties,TAIR_CONFIG_SERVERS, TAIR_GROUP_NAME, TAIR_NAMESPACE);
    			updateErrorCount(isTairWriteSucess);
    		}
    	}catch (Exception  e) {
		}
	}

    /**
    * @return ����дTair��Ⱥ�Ƿ�ɹ�(�����쳣���߷���˴���)
    */
   private boolean writeToTair(Properties properties, String configServers, String groupName, String tairNamespace) {
//		int namespace = getNamespace(properties, tairNamespace);
//		TairManager tairManager = getTairManager(properties, configServers,groupName);
//		ResultCode code = null;
//		try {
//			if (attributes != null && attributes.size() > 0) { // �ǿ�ʱ����
//				int expireTime = session.getMaxTairExpiredInterval(); // ʧЧʱ��45����
//				code = tairManager.put(namespace, getId(),(Serializable) attributes, 0,expireTime);
//			} else { // ��ʱɾ��
//				code = tairManager.delete(namespace, getId());
//			}
//			if (code.isSuccess()) {
//				return true;
//			}
//			if (!code.isSuccess()) { // ��������
//				ServerSessionList.addWarn("tairдʧ�ܣ�" + code.getMessage());
//			}
//		} catch (NullPointerException e) {
//			return false;
//		}
		return false;
	}

   /*
    * ��Tair��Ⱥû�йҵ�����ȡTair���ݣ���
    *  return true  :  ��ȡ�ɹ�  errorCount.getAndDecrement(1)
    *  return false :  ��ȡʧ��  errorCount.getAndAdd(1);
    */
   private boolean readFromTair() {
		Properties properties = getClientVersionProperties();
		boolean firstTairDown = isFirstTairDown();
		if (!firstTairDown) {
			boolean isTairReadSucess = readFromTair(properties,TAIR_CONFIG_SERVERS, TAIR_GROUP_NAME, TAIR_NAMESPACE);
			updateErrorCount(isTairReadSucess);// ���´��������
			lastReadTairTime = System.currentTimeMillis()/1000;
			return isTairReadSucess;
		} else {
			 // Tair�ҵ�������£���鵱ǰʱ����ϴη���ʱ�� �Ƿ񳬹�5���ӣ��������5�������Իָ���tair
			tryToReadTair(lastReadTairTime);
			return  false;
		}

	}

   /**
    *���ζ�tair��tair��Ⱥ�Ƿ��ѹ�(�����쳣���߷���˴���)
    * @param properties
    * @param configServers
    * @param groupName
    * @param tairNamespace
    * @param tairName
    * @return ���ζ�tair��Ⱥ�Ƿ�ɹ�(�����쳣���߷���˴���)
    */
   @SuppressWarnings("unchecked")
private boolean readFromTair(Properties properties, String configServers, String groupName, String tairNamespace) {
//		int namespace           = getNamespace(properties, tairNamespace);
//		TairManager tairManager = getTairManager(properties, configServers,groupName);
//		Result<DataEntry> result;
//		try {
//			result = tairManager.get(namespace, getId());
//		} catch (NullPointerException e) {
//			ServerSessionList.addWarn("DefaultTairManager ����ʧ�ܣ����ܴ�tair��д����"+ e.getMessage());
//			return false;
//		}
//		ResultCode code = result.getRc();
//		if (isTairHit(code)) { // ����
//			Object value = result.getValue().getValue();
//			if (value instanceof Map) {
//				Map<String, Object> values = (Map<String, Object>) value;
//				attributes.putAll(values);
//				setVersion(result.getValue().getVersion());
//				return true;
//			}
//		}
//		version = 0;
//		if (code.isSuccess()) {
//			return true;
//		}
//		  ServerSessionList.addWarn("tair��ʧ�ܣ�" + code.getMessage());
		return false;
	}

   /*
    * Tair�ҵ��󣬳���5���Ӻ��Զ��������¶�Tair
    */
   private  void tryToReadTair(long lastReadTairTime){
   	if((System.currentTimeMillis()/1000 - lastReadTairTime)>300){
   		 errorCount.getAndDecrement();
   		 readFromTair();
   	}

   }
    /**
     *
     * @param isTairOrWriteReadSucess
     * ��¼Tair������־����ر���
     */
    private void updateErrorCount(boolean isTairOrWriteReadSucess) {
        if (isTairOrWriteReadSucess) {
        	 decrementIfGreaterThenZero(errorCount);
        } else {
        	 if (errorCount.incrementAndGet() == ERROR_COUNT_LIMIT ) {
        		 ServerSessionList.addError("TairReadAndWriteErrorCount:"+errorCount.get());
        		 ServerSessionList.addError("Tair group halting");
             }
        	 else {
        	        ServerSessionList.addWarn("TairReadAndWriteErrorCount:"+errorCount.get());
        	 }
        }
    }

    private boolean isFirstTairDown() {
        return errorCount.get() > ERROR_COUNT_LIMIT;
    }


    private void decrementIfGreaterThenZero(AtomicInteger i) {
        while (true) {
            int current = i.get();
            if (current > 0) {
                int next = current - 1;
                if (i.compareAndSet(current, next)) {
                    return;
                }
            } else {
                return;
            }
        }
    }



    public boolean isTairDataChanged() {
		return tairDataChanged;
	}

	public void setTairDataChanged(boolean tairDataChanged) {
		this.tairDataChanged = tairDataChanged;
	}



    public void onSessionReady() {
        //readFromTair();
    }



//    private boolean isTairHit(ResultCode code) {
//        return ResultCode.SUCCESS.equals(code);
//    }

    private String getProperty(Properties properties, String name) {
        String property = properties.getProperty(name);
        if (property == null) {
            throw new IllegalConfigException("��������" + name + "����");
        }
        return property;
    }

//    private TairManager getTairManager(Properties properties, String configServers, String groupName) {
//        String configServersProperty = getProperty(properties, configServers);
//        String groupNameProperty = getProperty(properties, groupName);
//        return TairManagerFactory.getInstance(configServersProperty, groupNameProperty);
//    }

    private int getNamespace(Properties properties, String namespace) {
        String namespaceProperty = getProperty(properties, namespace);
        try {
            return Integer.parseInt(namespaceProperty);
        } catch (NumberFormatException e) {
            throw new IllegalConfigException(namespace + "����������", e);
        }
    }

    /*
     * tair���ݸ�����ֱ��дtair; ͬʱ�൱�ڸ���tair��Ч��
     * ��������Ǹ�����Ч�ڸ����ȶ�tair����дtair;
     * @see com.taobao.session.SessionStore#commit()
     */
  	public void commit() {
  		if (tairDataChanged ){
  			writeToTair();
  			return ;
  		}
  		if (expiredTimeChanged ) {
  			readFromTair();
  			writeToTair();
  		}
  	}

	public boolean isExpiredTimeChanged() {
		return expiredTimeChanged;
	}

	public void setExpiredTimeChanged(boolean expiredTimeChanged) {
		this.expiredTimeChanged = expiredTimeChanged;
	}

    private String getId() {
        return session.getId();
    }

    private Properties getClientVersionProperties() {
        return session.getClientVersionProperties();
    }

    private Properties getLatestVersionProperties() {
        return session.getLatestVersionProperties();
    }

	public static int getErrorCount() {
		return errorCount.get();
	}

	public static void setErrorCount() {
		 errorCount.set(0);
	}
    // ���·���ֻ���ڵ�Ԫ����

    void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    Map<String, Object> getAttributes() {
        return attributes;
    }

    int getVersion() {
        return version;
    }

    void setVersion(int version) {
        this.version = version;
    }

//    public static void main(String args[]){
//        TairManager tairManager =  TairManagerFactory.getInstance("tair2.config-vip.taobao.net:5198", "group_1");
//        System.out.println(tairManager.put(1000, "fenghao","hd",8,4500));
//        System.out.println(tairManager.get( 1000, "fenghao")) ;
//    }
}
