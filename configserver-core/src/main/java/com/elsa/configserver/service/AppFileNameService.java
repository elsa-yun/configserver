package com.elsa.configserver.service;

import java.util.List;

import com.elsa.configserver.domain.AppFileNameDO;
import com.elsa.configserver.exception.ServiceException;
import com.elsa.configserver.util.Page;

/**
 * app应用里配置文件的名称 Service
 * 
 * @author haisheng.long 2015-01-29 13:53:32
 */
public interface AppFileNameService {

	/**
	 * 插入 app应用里配置文件的名称
	 * 
	 * @param appFileNameDO
	 * @return 主键
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-29 13:53:32
	 */
	Long insert(AppFileNameDO appFileNameDO) throws ServiceException;

	/**
	 * 根据AppFileNameDO对象更新 app应用里配置文件的名称
	 * 
	 * @param appFileNameDO
	 * @param isAllField
	 *            是否更新所有字段(false 动态更新字段，true 更新所有字段,传null或false将动态更新，建议动态更新)
	 * @return 更新行数
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-29 13:53:32
	 */
	int update(AppFileNameDO appFileNameDO, boolean isAllField) throws ServiceException;

	// /**
	// * 根据ID更新 app应用里配置文件的名称全部字段
	// * @param appFileNameDO
	// * @return 更新行数
	// * @throws ServiceException
	// * @author longhaisheng 2015-01-29 13:53:32
	// */
	// int updateById(AppFileNameDO appFileNameDO) throws ServiceException;

	/**
	 * 根据ID删除 app应用里配置文件的名称
	 * 
	 * @param id
	 * @return 物理删除行
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-29 13:53:32
	 */
	int deleteById(Long id) throws ServiceException;

	// /**
	// * 动态更新 app应用里配置文件的名称部分字段
	// * @param appFileNameDO
	// * @return 更新行数
	// * @throws ServiceException
	// * @author longhaisheng 2015-01-29 13:53:32
	// */
	// int updateDynamic(AppFileNameDO appFileNameDO) throws ServiceException;

	/**
	 * 根据ID查询 一个 app应用里配置文件的名称
	 * 
	 * @param id
	 * @return AppFileNameDO
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-29 13:53:32
	 */
	AppFileNameDO selectById(Long id) throws ServiceException;

	/**
	 * 根据 app应用里配置文件的名称 动态返回记录数
	 * 
	 * @param appFileNameDO
	 * @return 记录数
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-29 13:53:32
	 */
	Long selectCountDynamic(AppFileNameDO appFileNameDO) throws ServiceException;

	/**
	 * 动态返回 app应用里配置文件的名称 列表
	 * 
	 * @param appFileNameDO
	 * @return List<AppFileNameDO>
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-29 13:53:32
	 */
	List<AppFileNameDO> selectDynamic(AppFileNameDO appFileNameDO) throws ServiceException;

	/**
	 * 动态返回 app应用里配置文件的名称 分页列表
	 * 
	 * @param appFileNameDO
	 * @return Page<AppFileNameDO>
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-29 13:53:32
	 */
	Page<AppFileNameDO> queryPageListByAppFileNameDO(AppFileNameDO appFileNameDO);

	/**
	 * 动态返回 app应用里配置文件的名称 分页列表
	 * 
	 * @param appFileNameDO
	 * @param startPage
	 *            起始页
	 * @param pageSize
	 *            每页记录数
	 * @return Page<AppFileNameDO>
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-29 13:53:32
	 */
	Page<AppFileNameDO> queryPageListByAppFileNameDOAndStartPageSize(AppFileNameDO appFileNameDO, int startPage, int pageSize);

	List<AppFileNameDO> queryByAppIds(List<Integer> appIds) throws ServiceException;

}
