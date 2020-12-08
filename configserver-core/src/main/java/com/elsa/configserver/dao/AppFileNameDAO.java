package com.elsa.configserver.dao;

import java.util.List;

import com.elsa.configserver.domain.AppFileNameDO;
import com.elsa.configserver.exception.DAOException;

/**
 * app应用里配置文件的名称 DAO
 * 
 * @author haisheng.Long 2015-01-29 13:53:32
 */
public interface AppFileNameDAO {

	/**
	 * 插入 app应用里配置文件的名称
	 * 
	 * @param appFileNameDO
	 * @return 主键
	 * @throws DAOException
	 * @author longhaisheng 2015-01-29 13:53:32
	 */
	Long insert(AppFileNameDO appFileNameDO) throws DAOException;

	/**
	 * 根据ID更新 app应用里配置文件的名称全部属性
	 * 
	 * @param appFileNameDO
	 * @return 更新行数
	 * @throws DAOException
	 * @author longhaisheng 2015-01-29 13:53:32
	 */
	Integer update(AppFileNameDO appFileNameDO) throws DAOException;

	/**
	 * 根据ID删除 app应用里配置文件的名称
	 * 
	 * @param id
	 * @return 删除行数
	 * @throws DAOException
	 * @author longhaisheng 2015-01-29 13:53:32
	 */
	Integer deleteById(Long id) throws DAOException;

	/**
	 * 动态更新 app应用里配置文件的名称部分属性，包括全部
	 * 
	 * @param appFileNameDO
	 * @return 更新行数
	 * @throws DAOException
	 * @author longhaisheng 2015-01-29 13:53:32
	 */
	Integer updateDynamic(AppFileNameDO appFileNameDO) throws DAOException;

	/**
	 * 根据ID查询 一个 app应用里配置文件的名称
	 * 
	 * @param id
	 * @return AppFileNameDO
	 * @throws DAOException
	 * @author longhaisheng 2015-01-29 13:53:32
	 */
	AppFileNameDO selectById(Long id) throws DAOException;

	/**
	 * 根据 app应用里配置文件的名称 动态返回记录数
	 * 
	 * @param appFileNameDO
	 * @return 记录条数
	 * @throws DAOException
	 * @author longhaisheng 2015-01-29 13:53:32
	 */
	Long selectCountDynamic(AppFileNameDO appFileNameDO) throws DAOException;

	/**
	 * 根据 app应用里配置文件的名称 动态返回 app应用里配置文件的名称 列表
	 * 
	 * @param appFileNameDO
	 * @return List<AppFileNameDO>
	 * @throws DAOException
	 * @author longhaisheng 2015-01-29 13:53:32
	 */
	List<AppFileNameDO> selectDynamic(AppFileNameDO appFileNameDO) throws DAOException;

	/**
	 * 根据 app应用里配置文件的名称 动态返回 app应用里配置文件的名称 Limit 列表
	 * 
	 * @param appFileNameDO
	 *            start,pageSize属性必须指定
	 * @return List<AppFileNameDO>
	 * @throws DAOException
	 * @author longhaisheng 2015-01-29 13:53:32
	 */
	List<AppFileNameDO> selectDynamicPageQuery(AppFileNameDO appFileNameDO) throws DAOException;

	List<AppFileNameDO> queryByAppIds(List<Integer> appIds) throws DAOException;
}
