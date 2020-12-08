package com.elsa.configserver.dao;

import java.util.List;

import com.elsa.configserver.domain.AppNameDO;
import com.elsa.configserver.exception.DAOException;

/**
 * DAO
 * 
 * @author haisheng.Long 2015-01-28 18:51:19
 */
public interface AppNameDAO {

	/**
	 * 插入
	 * 
	 * @param appNameDO
	 * @return 主键
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Long insert(AppNameDO appNameDO) throws DAOException;

	/**
	 * 根据ID更新 全部属性
	 * 
	 * @param appNameDO
	 * @return 更新行数
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Integer update(AppNameDO appNameDO) throws DAOException;

	/**
	 * 根据ID删除
	 * 
	 * @param id
	 * @return 删除行数
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Integer deleteById(Long id) throws DAOException;

	/**
	 * 动态更新 部分属性，包括全部
	 * 
	 * @param appNameDO
	 * @return 更新行数
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Integer updateDynamic(AppNameDO appNameDO) throws DAOException;

	/**
	 * 根据ID查询 一个
	 * 
	 * @param id
	 * @return AppNameDO
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	AppNameDO selectById(Long id) throws DAOException;

	/**
	 * 根据 动态返回记录数
	 * 
	 * @param appNameDO
	 * @return 记录条数
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Long selectCountDynamic(AppNameDO appNameDO) throws DAOException;

	/**
	 * 根据 动态返回 列表
	 * 
	 * @param appNameDO
	 * @return List<AppNameDO>
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	List<AppNameDO> selectDynamic(AppNameDO appNameDO) throws DAOException;

	/**
	 * 根据 动态返回 Limit 列表
	 * 
	 * @param appNameDO
	 *            start,pageSize属性必须指定
	 * @return List<AppNameDO>
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	List<AppNameDO> selectDynamicPageQuery(AppNameDO appNameDO) throws DAOException;
}
