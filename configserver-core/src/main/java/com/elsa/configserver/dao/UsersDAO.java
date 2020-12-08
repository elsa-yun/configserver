package com.elsa.configserver.dao;

import java.util.List;

import com.elsa.configserver.domain.UsersDO;
import com.elsa.configserver.exception.DAOException;

/**
 * 用户 DAO
 * 
 * @author haisheng.Long 2015-01-28 18:51:19
 */
public interface UsersDAO {

	/**
	 * 插入 用户
	 * 
	 * @param usersDO
	 * @return 主键
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Long insert(UsersDO usersDO) throws DAOException;

	/**
	 * 根据ID更新 用户全部属性
	 * 
	 * @param usersDO
	 * @return 更新行数
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Integer update(UsersDO usersDO) throws DAOException;

	/**
	 * 根据ID删除 用户
	 * 
	 * @param id
	 * @return 删除行数
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Integer deleteById(Long id) throws DAOException;

	/**
	 * 动态更新 用户部分属性，包括全部
	 * 
	 * @param usersDO
	 * @return 更新行数
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Integer updateDynamic(UsersDO usersDO) throws DAOException;

	/**
	 * 根据ID查询 一个 用户
	 * 
	 * @param id
	 * @return UsersDO
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	UsersDO selectById(Long id) throws DAOException;

	/**
	 * 根据 用户 动态返回记录数
	 * 
	 * @param usersDO
	 * @return 记录条数
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Long selectCountDynamic(UsersDO usersDO) throws DAOException;

	/**
	 * 根据 用户 动态返回 用户 列表
	 * 
	 * @param usersDO
	 * @return List<UsersDO>
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	List<UsersDO> selectDynamic(UsersDO usersDO) throws DAOException;

	/**
	 * 根据 用户 动态返回 用户 Limit 列表
	 * 
	 * @param usersDO
	 *            start,pageSize属性必须指定
	 * @return List<UsersDO>
	 * @throws DAOException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	List<UsersDO> selectDynamicPageQuery(UsersDO usersDO) throws DAOException;
}
