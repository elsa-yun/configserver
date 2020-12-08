package com.elsa.configserver.service;

import java.util.List;

import com.elsa.configserver.domain.AppNameDO;
import com.elsa.configserver.exception.ServiceException;
import com.elsa.configserver.util.Page;

/**
 * Service
 * 
 * @author haisheng.long 2015-01-28 18:51:19
 */
public interface AppNameService {

	/**
	 * 插入
	 * 
	 * @param appNameDO
	 * @return 主键
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Long insert(AppNameDO appNameDO) throws ServiceException;

	/**
	 * 根据AppNameDO对象更新
	 * 
	 * @param appNameDO
	 * @param isAllField
	 *            是否更新所有字段(false 动态更新字段，true 更新所有字段,传null或false将动态更新，建议动态更新)
	 * @return 更新行数
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	int update(AppNameDO appNameDO, boolean isAllField) throws ServiceException;

	// /**
	// * 根据ID更新 全部字段
	// * @param appNameDO
	// * @return 更新行数
	// * @throws ServiceException
	// * @author longhaisheng 2015-01-28 18:51:19
	// */
	// int updateById(AppNameDO appNameDO) throws ServiceException;

	/**
	 * 根据ID删除
	 * 
	 * @param id
	 * @return 物理删除行
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	int deleteById(Long id) throws ServiceException;

	// /**
	// * 动态更新 部分字段
	// * @param appNameDO
	// * @return 更新行数
	// * @throws ServiceException
	// * @author longhaisheng 2015-01-28 18:51:19
	// */
	// int updateDynamic(AppNameDO appNameDO) throws ServiceException;

	/**
	 * 根据ID查询 一个
	 * 
	 * @param id
	 * @return AppNameDO
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	AppNameDO selectById(Long id) throws ServiceException;

	/**
	 * 根据 动态返回记录数
	 * 
	 * @param appNameDO
	 * @return 记录数
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Long selectCountDynamic(AppNameDO appNameDO) throws ServiceException;

	/**
	 * 动态返回 列表
	 * 
	 * @param appNameDO
	 * @return List<AppNameDO>
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	List<AppNameDO> selectDynamic(AppNameDO appNameDO) throws ServiceException;

	/**
	 * 动态返回 分页列表
	 * 
	 * @param appNameDO
	 * @return Page<AppNameDO>
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Page<AppNameDO> queryPageListByAppNameDO(AppNameDO appNameDO);

	/**
	 * 动态返回 分页列表
	 * 
	 * @param appNameDO
	 * @param startPage
	 *            起始页
	 * @param pageSize
	 *            每页记录数
	 * @return Page<AppNameDO>
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Page<AppNameDO> queryPageListByAppNameDOAndStartPageSize(AppNameDO appNameDO, int startPage, int pageSize);

}
