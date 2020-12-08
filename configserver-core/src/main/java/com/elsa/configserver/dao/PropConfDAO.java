package com.elsa.configserver.dao;

import java.util.List;

import com.elsa.configserver.domain.PropConfDO;
import com.elsa.configserver.exception.DAOException;

/**
 * 应用配置项 DAO
 * 
 * @author haisheng.Long 2015-01-28 18:51:19
 */
public interface PropConfDAO {

	/**
	 * 插入 应用配置项
	 * 
	 * @param propConfDO
	 * @return 主键
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Long insert(PropConfDO propConfDO) throws DAOException;

	/**
	 * 根据ID更新 应用配置项全部属性
	 * 
	 * @param propConfDO
	 * @return 更新行数
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Integer update(PropConfDO propConfDO) throws DAOException;

	/**
	 * 根据ID删除 应用配置项
	 * 
	 * @param id
	 * @return 删除行数
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Integer deleteById(Long id) throws DAOException;

	/**
	 * 动态更新 应用配置项部分属性，包括全部
	 * 
	 * @param propConfDO
	 * @return 更新行数
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Integer updateDynamic(PropConfDO propConfDO) throws DAOException;

	/**
	 * 根据ID查询 一个 应用配置项
	 * 
	 * @param id
	 * @return PropConfDO
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	PropConfDO selectById(Long id) throws DAOException;

	/**
	 * 根据 应用配置项 动态返回记录数
	 * 
	 * @param propConfDO
	 * @return 记录条数
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Long selectCountDynamic(PropConfDO propConfDO) throws DAOException;

	/**
	 * 根据 应用配置项 动态返回 应用配置项 列表
	 * 
	 * @param propConfDO
	 * @return List<PropConfDO>
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	List<PropConfDO> selectDynamic(PropConfDO propConfDO) throws DAOException;

	/**
	 * 根据 应用配置项 动态返回 应用配置项 Limit 列表
	 * 
	 * @param propConfDO
	 *            start,pageSize属性必须指定
	 * @return List<PropConfDO>
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	List<PropConfDO> selectDynamicPageQuery(PropConfDO propConfDO) throws DAOException;

	PropConfDO selectOne(PropConfDO propConfDO) throws DAOException;
}
