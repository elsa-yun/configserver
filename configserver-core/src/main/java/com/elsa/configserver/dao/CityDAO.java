package com.elsa.configserver.dao;

import java.util.List;

import com.elsa.configserver.domain.CityDO;
import com.elsa.configserver.exception.DAOException;

/**
 * DAO
 * 
 * @author haisheng.Long 2015-01-28 14:58:14
 */
public interface CityDAO {

	/**
	 * 插入
	 * 
	 * @param cityDO
	 * @return 主键
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 14:58:14
	 */
	Long insert(CityDO cityDO) throws DAOException;

	/**
	 * 根据ID更新 全部属性
	 * 
	 * @param cityDO
	 * @return 更新行数
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 14:58:14
	 */
	Integer update(CityDO cityDO) throws DAOException;

	/**
	 * 根据ID删除
	 * 
	 * @param id
	 * @return 删除行数
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 14:58:14
	 */
	Integer deleteById(Long id) throws DAOException;

	/**
	 * 动态更新 部分属性，包括全部
	 * 
	 * @param cityDO
	 * @return 更新行数
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 14:58:14
	 */
	Integer updateDynamic(CityDO cityDO) throws DAOException;

	/**
	 * 根据ID查询 一个
	 * 
	 * @param id
	 * @return CityDO
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 14:58:14
	 */
	CityDO selectById(Long id, boolean readSlave) throws DAOException;

	/**
	 * 根据 动态返回记录数
	 * 
	 * @param cityDO
	 * @return 记录条数
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 14:58:14
	 */
	Long selectCountDynamic(CityDO cityDO) throws DAOException;

	/**
	 * 根据 动态返回 列表
	 * 
	 * @param cityDO
	 * @return List<CityDO>
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 14:58:14
	 */
	List<CityDO> selectDynamic(CityDO cityDO) throws DAOException;

	/**
	 * 根据 动态返回 Limit 列表
	 * 
	 * @param cityDO
	 *            start,pageSize属性必须指定
	 * @return List<CityDO>
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 14:58:14
	 */
	List<CityDO> selectDynamicPageQuery(CityDO cityDO) throws DAOException;
}
