package com.elsa.configserver.dao;

import java.util.List;

import com.elsa.configserver.domain.OpraterLogDO;
import com.elsa.configserver.exception.DAOException;

/**
 * 操作日志 DAO
 * 
 * @author haisheng.Long 2015-01-28 18:51:19
 */
public interface OpraterLogDAO {

	/**
	 * 插入 操作日志
	 * 
	 * @param opraterLogDO
	 * @return 主键
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Long insert(OpraterLogDO opraterLogDO) throws DAOException;

	/**
	 * 根据ID更新 操作日志全部属性
	 * 
	 * @param opraterLogDO
	 * @return 更新行数
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Integer update(OpraterLogDO opraterLogDO) throws DAOException;

	/**
	 * 根据ID删除 操作日志
	 * 
	 * @param id
	 * @return 删除行数
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Integer deleteById(Long id) throws DAOException;

	/**
	 * 动态更新 操作日志部分属性，包括全部
	 * 
	 * @param opraterLogDO
	 * @return 更新行数
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Integer updateDynamic(OpraterLogDO opraterLogDO) throws DAOException;

	/**
	 * 根据ID查询 一个 操作日志
	 * 
	 * @param id
	 * @return OpraterLogDO
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	OpraterLogDO selectById(Long id) throws DAOException;

	/**
	 * 根据 操作日志 动态返回记录数
	 * 
	 * @param opraterLogDO
	 * @return 记录条数
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Long selectCountDynamic(OpraterLogDO opraterLogDO) throws DAOException;

	/**
	 * 根据 操作日志 动态返回 操作日志 列表
	 * 
	 * @param opraterLogDO
	 * @return List<OpraterLogDO>
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	List<OpraterLogDO> selectDynamic(OpraterLogDO opraterLogDO) throws DAOException;

	/**
	 * 根据 操作日志 动态返回 操作日志 Limit 列表
	 * 
	 * @param opraterLogDO
	 *            start,pageSize属性必须指定
	 * @return List<OpraterLogDO>
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	List<OpraterLogDO> selectDynamicPageQuery(OpraterLogDO opraterLogDO) throws DAOException;
}
