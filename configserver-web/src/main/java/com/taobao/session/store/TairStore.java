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
     * 容灾考虑，两个Tair集群需要同步
     * 二期评审后由tair自己集群容灾，session中不负责
     */
//    private static final String TAIR_NAMESPACE_2 = "tair2.namespace";

//    private static final String TAIR_GROUP_NAME_2 = "tair2.groupName";

//    private static final String TAIR_CONFIG_SERVERS_2 = "tair2.configServers";

//    private static final String FIRST_TAIR_NAME = "Tair";

//    private static final int FIRST_TAIR = 1;

    private static final int ERROR_COUNT_LIMIT = 1000;

    /**
     * 本线程池用于写入备份集群，本线程池不限制大小，因为一个写入任务由一个请求线程创建，所以任务的个数与
     * jboss处理请求的线程数相同，即本线程池不会无限膨胀
     */
  //   private static final ExecutorService writeToSecondTairThreadPool = Executors.newCachedThreadPool();

    private static final AtomicInteger configVersion = new AtomicInteger(0);

    private static  AtomicInteger errorCount = new AtomicInteger(0);

    /**
     * 由于有两条线程(一条是请求线程，另一条是线程池中的线程)同时访问以下属性，所以这些属性都需要加上volatile修饰
     */
    private volatile Map<String, Object> attributes;

//	/private volatile int tair;

	private volatile int version = 0;

	private volatile TaobaoSession session;

	/*
	 * tair 服务端数据更新标志位
	 */
	private volatile boolean tairDataChanged = false;

	/*
	 * tair更新时间更新标志位
	 */
	private volatile boolean expiredTimeChanged = false;

	/*
	 * 是否有过读tair操作标志
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
     *  如果有新的配置推送过来，检查是否重置Tair读写失败计数器归零。
     */
    private void checkAndResetErrorCount(Properties properties) {
        int newVersion = getPropertiesVersion(properties);
        int oldVersion = configVersion.getAndSet(newVersion);
        if (oldVersion != newVersion) { // 版本已升级
            // 集群错误恢复后，推送一把重置错误数量
            String reset = properties.getProperty(TAIR_RESET_ERROR_COUNT);
            if (!StringUtils.isBlank(reset)) {
                 errorCount.set(0);
                 ServerSessionList.addInfo("已重置errorCount，tair集群恢复");
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
            // XXX 注意，这里调用了value上的toString()，而不是保存value对象本身
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
	 * @category  session信息写入Tair，写入失败计数器++ ，写入成功计数器--
	 **/
    private void writeToTair() {
    	try{
    		final Properties properties = getLatestVersionProperties();
    		// 只在写入时，即每个请求结束时检查reset，不在读取时检查，减少检查次数
    		checkAndResetErrorCount(properties);
    		boolean firstTairDown = isFirstTairDown();
    		 // 判断Tair集群是否已挂
    		if (!firstTairDown) {
    			boolean isTairWriteSucess = writeToTair(properties,TAIR_CONFIG_SERVERS, TAIR_GROUP_NAME, TAIR_NAMESPACE);
    			updateErrorCount(isTairWriteSucess);
    		}
    	}catch (Exception  e) {
		}
	}

    /**
    * @return 本次写Tair集群是否成功(网络异常或者服务端错误)
    */
   private boolean writeToTair(Properties properties, String configServers, String groupName, String tairNamespace) {
//		int namespace = getNamespace(properties, tairNamespace);
//		TairManager tairManager = getTairManager(properties, configServers,groupName);
//		ResultCode code = null;
//		try {
//			if (attributes != null && attributes.size() > 0) { // 非空时设置
//				int expireTime = session.getMaxTairExpiredInterval(); // 失效时间45分钟
//				code = tairManager.put(namespace, getId(),(Serializable) attributes, 0,expireTime);
//			} else { // 空时删除
//				code = tairManager.delete(namespace, getId());
//			}
//			if (code.isSuccess()) {
//				return true;
//			}
//			if (!code.isSuccess()) { // 其他错误
//				ServerSessionList.addWarn("tair写失败：" + code.getMessage());
//			}
//		} catch (NullPointerException e) {
//			return false;
//		}
		return false;
	}

   /*
    * 从Tair集群没有挂掉，读取Tair数据，、
    *  return true  :  读取成功  errorCount.getAndDecrement(1)
    *  return false :  读取失败  errorCount.getAndAdd(1);
    */
   private boolean readFromTair() {
		Properties properties = getClientVersionProperties();
		boolean firstTairDown = isFirstTairDown();
		if (!firstTairDown) {
			boolean isTairReadSucess = readFromTair(properties,TAIR_CONFIG_SERVERS, TAIR_GROUP_NAME, TAIR_NAMESPACE);
			updateErrorCount(isTairReadSucess);// 更新错误计数器
			lastReadTairTime = System.currentTimeMillis()/1000;
			return isTairReadSucess;
		} else {
			 // Tair挂掉的情况下，检查当前时间和上次访问时间 是否超过5分钟，如果超过5分钟则尝试恢复读tair
			tryToReadTair(lastReadTairTime);
			return  false;
		}

	}

   /**
    *本次读tair，tair集群是否已挂(网络异常或者服务端错误)
    * @param properties
    * @param configServers
    * @param groupName
    * @param tairNamespace
    * @param tairName
    * @return 本次读tair集群是否成功(网络异常或者服务端错误)
    */
   @SuppressWarnings("unchecked")
private boolean readFromTair(Properties properties, String configServers, String groupName, String tairNamespace) {
//		int namespace           = getNamespace(properties, tairNamespace);
//		TairManager tairManager = getTairManager(properties, configServers,groupName);
//		Result<DataEntry> result;
//		try {
//			result = tairManager.get(namespace, getId());
//		} catch (NullPointerException e) {
//			ServerSessionList.addWarn("DefaultTairManager 创建失败，不能从tair读写数据"+ e.getMessage());
//			return false;
//		}
//		ResultCode code = result.getRc();
//		if (isTairHit(code)) { // 命中
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
//		  ServerSessionList.addWarn("tair读失败：" + code.getMessage());
		return false;
	}

   /*
    * Tair挂掉后，超过5分钟后，自动尝试重新读Tair
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
     * 记录Tair错误日志，监控报警
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
            throw new IllegalConfigException("必须配置" + name + "属性");
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
            throw new IllegalConfigException(namespace + "必须是数字", e);
        }
    }

    /*
     * tair数据更新则直接写tair; 同时相当于更新tair有效期
     * 如果仅仅是更新有效期更新先读tair，后写tair;
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
    // 以下方法只用于单元测试

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
