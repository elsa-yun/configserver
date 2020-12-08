package com.elsa.configserver.service;

import java.util.List;

import com.elsa.configserver.domain.PropConfDO;
import com.elsa.configserver.exception.ServiceException;
import com.elsa.configserver.util.Page;

/**
 * 应用配置项 Service
 * 
 * @author haisheng.long 2015-01-28 18:51:19
 */
public interface PropConfService {

	/**
	 * 插入 应用配置项
	 * 
	 * @param propConfDO
	 * @return 主键
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Long insert(PropConfDO propConfDO) throws ServiceException;

	/**
	 * 根据PropConfDO对象更新 应用配置项
	 * 
	 * @param propConfDO
	 * @param isAllField
	 *            是否更新所有字段(false 动态更新字段，true 更新所有字段,传null或false将动态更新，建议动态更新)
	 * @return 更新行数
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	int update(PropConfDO propConfDO, boolean isAllField) throws ServiceException;

	// /**
	// * 根据ID更新 应用配置项全部字段
	// * @param propConfDO
	// * @return 更新行数
	// * @throws ServiceException
	// * @author longhaisheng 2015-01-28 18:51:19
	// */
	// int updateById(PropConfDO propConfDO) throws ServiceException;

	/**
	 * 根据ID删除 应用配置项
	 * 
	 * @param id
	 * @return 物理删除行
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	int deleteById(Long id) throws ServiceException;

	// /**
	// * 动态更新 应用配置项部分字段
	// * @param propConfDO
	// * @return 更新行数
	// * @throws ServiceException
	// * @author longhaisheng 2015-01-28 18:51:19
	// */
	// int updateDynamic(PropConfDO propConfDO) throws ServiceException;

	/**
	 * 根据ID查询 一个 应用配置项
	 * 
	 * @param id
	 * @return PropConfDO
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	PropConfDO selectById(Long id) throws ServiceException;

	/**
	 * 根据 应用配置项 动态返回记录数
	 * 
	 * @param propConfDO
	 * @return 记录数
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Long selectCountDynamic(PropConfDO propConfDO) throws ServiceException;

	/**
	 * 动态返回 应用配置项 列表
	 * 
	 * @param propConfDO
	 * @return List<PropConfDO>
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	List<PropConfDO> selectDynamic(PropConfDO propConfDO) throws ServiceException;

	/**
	 * 动态返回 应用配置项 分页列表
	 * 
	 * @param propConfDO
	 * @return Page<PropConfDO>
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Page<PropConfDO> queryPageListByPropConfDO(PropConfDO propConfDO);

	/**
	 * 动态返回 应用配置项 分页列表
	 * 
	 * @param propConfDO
	 * @param startPage
	 *            起始页
	 * @param pageSize
	 *            每页记录数
	 * @return Page<PropConfDO>
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Page<PropConfDO> queryPageListByPropConfDOAndStartPageSize(PropConfDO propConfDO, int startPage, int pageSize);

	PropConfDO selectOne(PropConfDO propConfDO);

}
