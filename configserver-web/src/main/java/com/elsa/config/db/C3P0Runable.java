package com.elsa.config.db;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.PriorityOrdered;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.elsa.config.db.C3P0Runable;
import com.elsa.config.db.MysqlDO;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.MtComboPooledDataSource;

/**
 * 
 * @author longhaisheng
 *
 */
public class C3P0Runable implements Runnable, PriorityOrdered {

	private static final String SELECT_1_AS_NUM_SQL = "select 1 as num";

	private static final String SLAVE = "slave";

	private Map<String, AbstractRoutingDataSource> dataSourceMap;

	private Map<String, ?> dbcpSourceMap;

	private Map<String, Object> slaveTargetDataSources = new HashMap<String, Object>();

	private Map<String, Object> removeList = new HashMap<String, Object>();

	private Set<String> removeDataSourceBeanIdSet = new HashSet<String>();

	private List<MysqlDO> mysqlSlaves = new ArrayList<MysqlDO>();

	private volatile boolean flag = true;

	private static final Log logger = LogFactory.getLog(C3P0Runable.class);

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	@Override
	public int getOrder() {
		return LOWEST_PRECEDENCE - 12;
	}

	@Override
	public void run() {
		if (flag) {
			this.watch();
		}
	}

	private synchronized void watch() {
		for (Map.Entry<String, AbstractRoutingDataSource> entry : dataSourceMap.entrySet()) {
			AbstractRoutingDataSource ards = entry.getValue();
			this.filterSlaveDataSource(ards);
		}
		for (Map.Entry<String, ?> entry : dbcpSourceMap.entrySet()) {
			if (entry.getKey().toLowerCase().contains(SLAVE)) {

				DataSource ds = null;
//				BasicDataSource bds = null;
				MysqlDO mysql = new MysqlDO();
//				if (entry.getValue() instanceof BasicDataSource) {
//					bds = (BasicDataSource) entry.getValue();
//					String[] get_ip_port = get_ip_port(bds.getUrl());
//					mysql.setDriverClassName(bds.getDriverClassName());
//					mysql.setUrl(bds.getUrl());
//					mysql.setPassWord(bds.getPassword());
//					mysql.setIp(get_ip_port[0]);
//					mysql.setPort(Integer.parseInt(get_ip_port[1]));
//					mysql.setDbName(get_db_name(bds.getUrl()));
//					mysql.setUserName(bds.getUsername());
//					mysql.setBeanId(entry.getKey());
//					mysqlSlaves.add(mysql);
//					ds = bds;
//				}

				ComboPooledDataSource cpds = null;
				if (entry.getValue() instanceof ComboPooledDataSource) {
					cpds = (ComboPooledDataSource) entry.getValue();
					String[] get_ip_port = get_ip_port(cpds.getJdbcUrl());
					mysql.setDriverClassName(cpds.getDriverClass());
					mysql.setUrl(cpds.getJdbcUrl());
					mysql.setPassWord(cpds.getPassword());
					mysql.setIp(get_ip_port[0]);
					mysql.setPort(Integer.parseInt(get_ip_port[1]));
					mysql.setDbName(get_db_name(cpds.getJdbcUrl()));
					mysql.setUserName(cpds.getUser());
					mysql.setBeanId(entry.getKey());
					mysqlSlaves.add(mysql);
					ds = cpds;
				}

//				MtComboPooledDataSource mcpds = null;
//				if (entry.getValue() instanceof MtComboPooledDataSource) {
//					mcpds = (MtComboPooledDataSource) entry.getValue();
//					String[] get_ip_port = get_ip_port(mcpds.getJdbcUrl());
//					mysql.setDriverClassName(mcpds.getDriverClass());
//					mysql.setUrl(mcpds.getJdbcUrl());
//					mysql.setPassWord(mcpds.getPassword());
//					mysql.setIp(get_ip_port[0]);
//					mysql.setPort(Integer.parseInt(get_ip_port[1]));
//					mysql.setDbName(get_db_name(mcpds.getJdbcUrl()));
//					mysql.setUserName(mcpds.getUser());
//					mysql.setBeanId(entry.getKey());
//					mysqlSlaves.add(mysql);
//					ds = mcpds;
//				}


				boolean watchOneNode = watchOneNode(ds);
				logger.error("aaaaaaaaaaaaaaaaaaaaaaa=>"+watchOneNode);
				if (watchOneNode) {
					addSlaveDataSource(mysql.getDbName());
				} else {
					removeSlaveDataSource(mysql.getDbName());
				}

			}
		}

	}

	private void removeSlaveDataSource(String dbname) {
		String dataSourceBeanId = "";
		for (MysqlDO m : mysqlSlaves) {
			if (m.getDbName().equals(dbname)) {
				dataSourceBeanId = m.getBeanId();
				break;
			}
		}

		if (!"".equals(dataSourceBeanId)) {
			for (Map.Entry<String, AbstractRoutingDataSource> ard : dataSourceMap.entrySet()) {
				AbstractRoutingDataSource ds = (AbstractRoutingDataSource) ard.getValue();
				removeSlaveDataSource(ds, dataSourceBeanId);
			}
		}

	}

	private void addSlaveDataSource(String dbname) {
		String dataSourceBeanId = "";
		for (MysqlDO m : mysqlSlaves) {
			if (m.getDbName().equals(dbname)) {
				dataSourceBeanId = m.getBeanId();
				break;
			}
		}

		Map<String, Object> returnAddMap = returnAddMapNew(dataSourceBeanId);

		for (Map.Entry<String, AbstractRoutingDataSource> ard : dataSourceMap.entrySet()) {
			AbstractRoutingDataSource ds = (AbstractRoutingDataSource) ard.getValue();
			addSlaveDataSource(ds, returnAddMap, dataSourceBeanId);
		}

	}

	private Map<String, Object> returnAddMapNew(String dataSourceBeanId) {
		Map<String, Object> map = new HashMap<String, Object>();

//		BasicDataSource bds = null;
		Object bean = dbcpSourceMap.get(dataSourceBeanId);
//		if (bean instanceof BasicDataSource) {
//			bds = (BasicDataSource) dbcpSourceMap.get(dataSourceBeanId);
//			for (Map.Entry<String, Object> ent : removeList.entrySet()) {
//				BasicDataSource as = (BasicDataSource) ent.getValue();
//				if (bds.getUrl().equals(as.getUrl())) {
//					map.put(ent.getKey(), ent.getValue());
//					break;
//				}
//			}
//		}

		ComboPooledDataSource cpds = null;
		if (bean instanceof ComboPooledDataSource) {
			cpds = (ComboPooledDataSource) dbcpSourceMap.get(dataSourceBeanId);
			for (Map.Entry<String, Object> ent : removeList.entrySet()) {
				ComboPooledDataSource as = (ComboPooledDataSource) ent.getValue();
				if (cpds.getJdbcUrl().equals(as.getJdbcUrl())) {
					map.put(ent.getKey(), ent.getValue());
					break;
				}
			}
		}

//		MtComboPooledDataSource mcpds = null;
//		if (bean instanceof MtComboPooledDataSource) {
//			mcpds = (MtComboPooledDataSource) dbcpSourceMap.get(dataSourceBeanId);
//			for (Map.Entry<String, Object> ent : removeList.entrySet()) {
//				MtComboPooledDataSource as = (MtComboPooledDataSource) ent.getValue();
//				if (mcpds.getJdbcUrl().equals(as.getJdbcUrl())) {
//					map.put(ent.getKey(), ent.getValue());
//					break;
//				}
//			}
//		}

		return map;
	}

	private void addSlaveDataSource(Object ards, Map<String, Object> addMap, String dataSourceBeanId) {
		boolean isNeedAdd = false;
		for (Map.Entry<String, Object> e : addMap.entrySet()) {
			if (removeList.containsKey(e.getKey())) {
				isNeedAdd = true;
			}
		}
		if (isNeedAdd) {
			logger.info("addDataSources isNeedAdd==>" + isNeedAdd);
			for (Map.Entry<String, AbstractRoutingDataSource> ent : dataSourceMap.entrySet()) {

				Field[] fieldlist = ards.getClass().getSuperclass().getDeclaredFields();
				for (int i = 0; i < fieldlist.length; i++) {
					Field f = fieldlist[i];
					f.setAccessible(true);
					String name = f.getName();
					if (name.equals("targetDataSources")) {
						try {
							Map<Object, Object> targetDataSources = (Map<Object, Object>) f.get(ards);
							for (Map.Entry<String, Object> e : addMap.entrySet()) {
								if (!targetDataSources.containsKey(e.getKey())) {
									targetDataSources.put(e.getKey(), e.getValue());
									removeList.remove(e.getKey());
									removeDataSourceBeanIdSet.remove(dataSourceBeanId);
									// logger.info("f addKey targetDataSources e.getKey()==>"
									// + e.getKey());
								}
							}
							f.set(ards, targetDataSources);
							call_after_properties_set(ards);
							for (Map.Entry<Object, Object> e : targetDataSources.entrySet()) {
							}
						} catch (IllegalArgumentException e) {
							logger.error(e.getMessage(), e);
						} catch (IllegalAccessException e) {
							logger.error(e.getMessage(), e);
						}
					}
				}

				// after remove view logs
				// for (Map.Entry<String, AbstractRoutingDataSource> drd :
				// dataSourceMap.entrySet()) {
				// Object ds = drd.getValue();
				// fieldlist =
				// ds.getClass().getSuperclass().getDeclaredFields();
				// for (int i = 0; i < fieldlist.length; i++) {
				// Field f = fieldlist[i];
				// f.setAccessible(true);
				// String name = f.getName();
				// if (name.equals("targetDataSources")) {
				// try {
				// Map<Object, Object> targetDataSources = (Map<Object, Object>)
				// f.get(ards);
				// for (Map.Entry<Object, Object> slave :
				// targetDataSources.entrySet()) {
				// // logger.info("add after targetDataSources key is==>"
				// // + slave.getKey());
				// }
				//
				// } catch (IllegalArgumentException e) {
				// logger.error(e.getMessage(), e);
				// } catch (IllegalAccessException e) {
				// logger.error(e.getMessage(), e);
				// }
				// }
				// }
				// }
			}
		}
	}

	private void removeSlaveDataSource(Object ards, String dataSourceBeanId) {
		if (removeDataSourceBeanIdSet.contains(dataSourceBeanId)) {
			return;
		} else {
			logger.info("removeSlaveDataSource dataSourceBeanId==>" + dataSourceBeanId);
		}

		Object o = dbcpSourceMap.get(dataSourceBeanId);
//		BasicDataSource bds = null;
//		if (o instanceof BasicDataSource) {
//			bds = (BasicDataSource) dbcpSourceMap.get(dataSourceBeanId);
//		}

		ComboPooledDataSource cpds = null;
		if (o instanceof ComboPooledDataSource) {
			cpds = (ComboPooledDataSource) dbcpSourceMap.get(dataSourceBeanId);
		}

//		MtComboPooledDataSource mcpds = null;
//		if (o instanceof MtComboPooledDataSource) {
//			mcpds = (MtComboPooledDataSource) dbcpSourceMap.get(dataSourceBeanId);
//		}

		Field[] fieldlist = ards.getClass().getSuperclass().getDeclaredFields();
		for (int i = 0; i < fieldlist.length; i++) {
			Field f = fieldlist[i];
			f.setAccessible(true);
			String name = f.getName();
			if (name.equals("targetDataSources")) {
				try {
					Map<Object, Object> targetDataSources = (Map<Object, Object>) f.get(ards);
					String removeKey = "";
					for (Map.Entry<Object, Object> entry : targetDataSources.entrySet()) {
//						if (null != bds) {
//							BasicDataSource value = (BasicDataSource) entry.getValue();
//							if (entry.getKey().toString().toLowerCase().contains(SLAVE) && value.getUrl().equals(bds.getUrl())) {
//								removeKey = entry.getKey().toString();
//								removeList.put(removeKey, entry.getValue());
//								break;
//							}
//						}

						if (null != cpds) {
							ComboPooledDataSource value = (ComboPooledDataSource) entry.getValue();
							if (entry.getKey().toString().contains(SLAVE) && value.getJdbcUrl().equals(cpds.getJdbcUrl())) {
								removeKey = entry.getKey().toString();
								removeList.put(removeKey, entry.getValue());
								break;
							}
						}

//						if (null != mcpds) {
//							MtComboPooledDataSource value = (MtComboPooledDataSource) entry.getValue();
//							if (entry.getKey().toString().contains(SLAVE) && value.getJdbcUrl().equals(mcpds.getJdbcUrl())) {
//								removeKey = entry.getKey().toString();
//								removeList.put(removeKey, entry.getValue());
//								break;
//							}
//						}

					}
					targetDataSources.remove(removeKey);
					removeDataSourceBeanIdSet.add(dataSourceBeanId);
					f.set(ards, targetDataSources);
					logger.info("f removeKey ==>" + removeKey);
					call_after_properties_set(ards);
					logger.info("f targetDataSources size==>" + targetDataSources.size());
					for (Map.Entry<Object, Object> entry : targetDataSources.entrySet()) {
						logger.info("after remove targetDataSources key==>" + entry.getKey());
					}
				} catch (IllegalArgumentException e) {
					logger.error(e.getMessage(), e);
				} catch (IllegalAccessException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}

		// after remove view logs
		// for (Map.Entry<String, AbstractRoutingDataSource> drd :
		// dataSourceMap.entrySet()) {
		// Object ds = drd.getValue();
		// fieldlist = ds.getClass().getSuperclass().getDeclaredFields();
		// for (int i = 0; i < fieldlist.length; i++) {
		// Field f = fieldlist[i];
		// f.setAccessible(true);
		// String name = f.getName();
		// if (name.equals("targetDataSources")) {
		// try {
		// Map<Object, Object> targetDataSources = (Map<Object, Object>)
		// f.get(ards);
		// for (Map.Entry<Object, Object> slave : targetDataSources.entrySet())
		// {
		// logger.info("move after targetDataSources key is==>" +
		// slave.getKey());
		// }
		//
		// } catch (IllegalArgumentException e) {
		// logger.error(e.getMessage(), e);
		// } catch (IllegalAccessException e) {
		// logger.error(e.getMessage(), e);
		// }
		// }
		// }
		// }
	}

	private void call_after_properties_set(Object ards) throws IllegalAccessException {
		Method m1 = null;
		try {
			m1 = ards.getClass().getSuperclass().getDeclaredMethod("afterPropertiesSet");
		} catch (NoSuchMethodException e) {
			logger.error(e.getMessage(), e);
		} catch (SecurityException e) {
			logger.error(e.getMessage(), e);
		}
		try {
			if (null != m1) {
				m1.invoke(ards);
			}
		} catch (InvocationTargetException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void filterSlaveDataSource(Object ards) {
		Field[] fieldlist = ards.getClass().getSuperclass().getDeclaredFields();
		for (int i = 0; i < fieldlist.length; i++) {
			Field f = fieldlist[i];
			f.setAccessible(true);
			String name = f.getName();
			if (name.equals("targetDataSources")) {
				try {
					Map<Object, Object> targetDataSources = (Map<Object, Object>) f.get(ards);
					// logger.info("filterSlaveDataSource targetDataSources size==>"
					// + targetDataSources.size());
					for (Map.Entry<Object, Object> entry : targetDataSources.entrySet()) {
						if (entry.getKey().toString().toLowerCase().contains(SLAVE)) {
							slaveTargetDataSources.put(entry.getKey().toString(), entry.getValue());
						}

					}
				} catch (IllegalArgumentException e) {
					logger.error(e.getMessage(), e);
				} catch (IllegalAccessException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		// logger.info("slaveTargetDataSources targetDataSources size==>" +
		// slaveTargetDataSources.size());
	}

	private static String[] get_ip_port(String dburl) {
		dburl = dburl.trim();
		String[] split = dburl.split("jdbc:mysql://");
		String[] split2 = split[1].split("/");
		String[] split3 = split2[0].split(":");
		return split3;
	}

	private static String get_db_name(String dburl) {
		String[] split = dburl.split("3306/");
		String dbname = "";
		if (split[1].indexOf("?") != -1) {
			String[] split2 = split[1].split("\\?");
			dbname = split2[0];
		} else {
			dbname = split[1];
		}
		return dbname.trim();
	}

	public List<MysqlDO> getMysqlSlaves() {
		return mysqlSlaves;
	}

	public void setMysqlSlaves(List<MysqlDO> mysqlSlaves) {
		this.mysqlSlaves = mysqlSlaves;
	}

	public Map<String, AbstractRoutingDataSource> getDataSourceMap() {
		return dataSourceMap;
	}

	public void setDataSourceMap(Map<String, AbstractRoutingDataSource> dataSourceMap) {
		this.dataSourceMap = dataSourceMap;
	}

	public Map<String, ?> getDbcpSourceMap() {
		return dbcpSourceMap;
	}

	public void setDbcpSourceMap(Map<String, ?> dbcpSourceMap) {
		this.dbcpSourceMap = dbcpSourceMap;
	}

	public Map<String, Object> getSlaveTargetDataSources() {
		return slaveTargetDataSources;
	}

	public void setSlaveTargetDataSources(Map<String, Object> slaveTargetDataSources) {
		this.slaveTargetDataSources = slaveTargetDataSources;
	}

	public Map<String, Object> getRemoveList() {
		return removeList;
	}

	public void setRemoveList(Map<String, Object> removeList) {
		this.removeList = removeList;
	}

	// ///////////////////////////////////////////////
	private static int executeQuery(DataSource dataSource, String sql, Map<String, Object> map) {
		Pattern p = Pattern.compile("(#[^#]*#)");
		Matcher m = p.matcher(sql);
		List<String> result = new ArrayList<String>();
		while (m.find()) {
			String group = m.group();
			result.add(group.substring(1, group.length() - 1));
			sql = sql.replaceAll(group, "?");
		}
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			logger.error("SQLException for:" + e.getMessage(), e);
		}
		int count = 0;
		PreparedStatement stmt = null;
		try {
			if (null != conn) {
				stmt = conn.prepareStatement(sql);
				int size = result.size();
				for (int j = 0; j < size; j++) {
					Object object = map.get(result.get(j));
					if (null != object) {
						stmt.setString(j + 1, object.toString());
					} else {
						stmt.setString(j + 1, null);
					}
				}
				count = stmt.executeUpdate();
			}
		} catch (SQLException e) {
//			logger.error("JDBC executeQuery exception for=>" + e.getMessage(), e);
		} finally {
			close(conn, stmt, null);
		}
		return count;
	}

	private static List<Map<String, String>> queryAll(DataSource dataSource, String sql, Map<String, Object> map) {
		Pattern p = Pattern.compile("(#[^#]*#)");
		Matcher m = p.matcher(sql);
		List<String> result = new ArrayList<String>();
		while (m.find()) {
			String group = m.group();
			result.add(group.substring(1, group.length() - 1));
			sql = sql.replaceAll(group, "?");
		}

		List<String> colums = new ArrayList<String>();// getColumNames(sql);
		colums.add("num");

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			logger.error("SQLException for:" + e.getMessage(), e);
		}
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			if (null != conn) {
				stmt = conn.prepareStatement(sql);
				int size = result.size();
				for (int j = 0; j < size; j++) {
					stmt.setString(j + 1, map.get(result.get(j)).toString());
				}
				rs = stmt.executeQuery();
				while (rs.next()) {
					Map<String, String> row = new HashMap<String, String>();
					for (String string : colums) {
						String value = rs.getString(string);
						row.put(string, value);
					}
					resultList.add(row);
				}
			}
		} catch (SQLException e) {
			logger.error("JDBC queryAll SQLException for:" + e.getMessage(), e);
		} finally {
			close(conn, stmt, rs);
		}
		return resultList;
	}

	private static void close(Connection conn, PreparedStatement stmt, ResultSet rs) {
		try {
			if (null != rs) {
				rs.close();
			}
		} catch (SQLException e) {
			// igore
			// logger.error("SQLException for:" + e.getMessage(), e);
		}
		try {
			if (null != stmt) {
				stmt.close();
			}
		} catch (SQLException e) {
			// igore
			// logger.error("SQLException for:" + e.getMessage(), e);
		}
		try {
			if (null != conn) {
				conn.close();
			}
		} catch (SQLException e) {
			// igore
			// logger.error("SQLException for:" + e.getMessage(), e);
		}
	}

	public static String parseDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	private static List<String> getColumNames(String sql) {
		List<String> list = new ArrayList<String>();
		String columns = sql.substring(sql.indexOf("select ") + 7, sql.indexOf("from "));
		if (null != columns) {
			for (String str : columns.split(",")) {
				str = str.trim();
				if (str.contains(" as ")) {
					String[] split = str.split(" as ");
					list.add(split[1].trim());
				} else if (str.contains(" ")) {
					str = str.replaceAll(" +", " ");
					String[] split = str.split(" ");
					list.add(split[1].trim());
				} else {
					list.add(str.trim());
				}
			}
		}
		return list;
	}

	// private boolean watchOneNode(DataSource dataSource) {
	// int timeout = 8000;
	// long waitTime = 0;
	// int retryTimes = 3;
	//
	// int tryCount = 0;
	// int failTimes = 0;
	// while (0 == timeout || timeout > waitTime) {
	// tryCount++;
	// if (tryCount > retryTimes + 1) {
	// break;
	// }
	//
	// long onceTimeOut = getOnceTimeOut(waitTime, timeout);
	// waitTime += onceTimeOut;
	//
	// try {
	// List<Map<String, String>> queryAll = queryAll(dataSource,
	// SELECT_1_AS_NUM_SQL, new HashMap<String, Object>());
	// if (null == queryAll || queryAll.isEmpty()) {
	// failTimes++;
	// } else {
	// //
	// logger.info("########################################################### num is =>"
	// // + queryAll.get(0).get("num"));
	// }
	// } catch (Exception e) {
	// failTimes++;
	// }
	// }
	// if (failTimes >= tryCount) {
	// return false;
	// }
	// return true;
	// }

	private boolean watchOneNode(DataSource dataSource) {
		int failTimes = 0;
		int count = 5;
		for (int i = 0; i < count; i++) {
			List<Map<String, String>> queryAll = null;
			try {
				queryAll = queryAll(dataSource, SELECT_1_AS_NUM_SQL, new HashMap<String, Object>());
			} catch (Exception e) {
				failTimes++;
			}
			if (null == queryAll || queryAll.isEmpty()) {
				failTimes++;
			} else {
//				logger.info("########################################################### num is =>" + queryAll.get(0).get("num") + " i is=>" + i);
			}
//			try {
//				Thread.sleep(200);
//			} catch (InterruptedException e) {
//				logger.error(e.getMessage(), e);
//			}
		}
		if (failTimes == count) {
//			logger.info("watchOneNode is false");
			return false;
		}
//		logger.info("watchOneNode is true");
		return true;
	}

	private long getOnceTimeOut(long waitTime, long timeout) {
		long onceTimeOut = 2000;
		long remainTime = timeout - waitTime;
		if (onceTimeOut > remainTime) {
			onceTimeOut = remainTime;
		}
		return onceTimeOut;
	}

}
