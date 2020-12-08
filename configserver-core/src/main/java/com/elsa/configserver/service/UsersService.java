package com.elsa.configserver.service;

import java.util.List;

import com.elsa.configserver.domain.UsersDO;
import com.elsa.configserver.exception.ServiceException;
import com.elsa.configserver.util.Page;

/**
 * 用户 Service
 * 
 * @author haisheng.long 2015-01-28 18:51:19
 */
public interface UsersService {

	/**
	 * 插入 用户
	 * 
	 * @param usersDO
	 * @return 主键
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Long insert(UsersDO usersDO) throws ServiceException;

	/**
	 * 根据UsersDO对象更新 用户
	 * 
	 * @param usersDO
	 * @param isAllField
	 *            是否更新所有字段(false 动态更新字段，true 更新所有字段,传null或false将动态更新，建议动态更新)
	 * @return 更新行数
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	int update(UsersDO usersDO, boolean isAllField) throws ServiceException;

	// /**
	// * 根据ID更新 用户全部字段
	// * @param usersDO
	// * @return 更新行数
	// * @throws ServiceException
	// * @author longhaisheng 2015-01-28 18:51:19
	// */
	// int updateById(UsersDO usersDO) throws ServiceException;

	/**
	 * 根据ID删除 用户
	 * 
	 * @param id
	 * @return 物理删除行
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	int deleteById(Long id) throws ServiceException;

	// /**
	// * 动态更新 用户部分字段
	// * @param usersDO
	// * @return 更新行数
	// * @throws ServiceException
	// * @author longhaisheng 2015-01-28 18:51:19
	// */
	// int updateDynamic(UsersDO usersDO) throws ServiceException;

	/**
	 * 根据ID查询 一个 用户
	 * 
	 * @param id
	 * @return UsersDO
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	UsersDO selectById(Long id) throws ServiceException;

	/**
	 * 根据 用户 动态返回记录数
	 * 
	 * @param usersDO
	 * @return 记录数
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Long selectCountDynamic(UsersDO usersDO) throws ServiceException;

	/**
	 * 动态返回 用户 列表
	 * 
	 * @param usersDO
	 * @return List<UsersDO>
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	List<UsersDO> selectDynamic(UsersDO usersDO) throws ServiceException;

	/**
	 * 动态返回 用户 分页列表
	 * 
	 * @param usersDO
	 * @return Page<UsersDO>
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Page<UsersDO> queryPageListByUsersDO(UsersDO usersDO);

	/**
	 * 动态返回 用户 分页列表
	 * 
	 * @param usersDO
	 * @param startPage
	 *            起始页
	 * @param pageSize
	 *            每页记录数
	 * @return Page<UsersDO>
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Page<UsersDO> queryPageListByUsersDOAndStartPageSize(UsersDO usersDO, int startPage, int pageSize);

}
