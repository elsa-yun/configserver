package com.elsa.configserver.service;

import java.util.List;

import com.elsa.configserver.domain.CityDO;
import com.elsa.configserver.exception.ServiceException;
import com.elsa.configserver.util.Page;

/**
 * Service
 * 
 * @author haisheng.long 2015-01-28 14:58:14
 */
public interface CityService {

	/**
	 * 插入
	 * 
	 * @param cityDO
	 * @return 主键
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 14:58:14
	 */
	Long insert(CityDO cityDO) throws ServiceException;

	/**
	 * 根据CityDO对象更新
	 * 
	 * @param cityDO
	 * @param isAllField
	 *            是否更新所有字段(false 动态更新字段，true 更新所有字段,传null或false将动态更新，建议动态更新)
	 * @return 更新行数
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 14:58:14
	 */
	int update(CityDO cityDO, boolean isAllField) throws ServiceException;

	// /**
	// * 根据ID更新 全部字段
	// * @param cityDO
	// * @return 更新行数
	// * @throws ServiceException
	// * @author longhaisheng 2015-01-28 14:58:14
	// */
	// int updateById(CityDO cityDO) throws ServiceException;

	/**
	 * 根据ID删除
	 * 
	 * @param id
	 * @return 物理删除行
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 14:58:14
	 */
	int deleteById(Long id) throws ServiceException;

	// /**
	// * 动态更新 部分字段
	// * @param cityDO
	// * @return 更新行数
	// * @throws ServiceException
	// * @author longhaisheng 2015-01-28 14:58:14
	// */
	// int updateDynamic(CityDO cityDO) throws ServiceException;

	/**
	 * 根据ID查询 一个
	 * 
	 * @param id
	 * @param newParam
	 *            TODO
	 * @return CityDO
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 14:58:14
	 */
	CityDO selectById(int id, int newParam) throws ServiceException;
	
	CityDO transactional(final int id, final int newParam) throws ServiceException;

	CityDO select(int id) throws ServiceException;

	/**
	 * 根据 动态返回记录数
	 * 
	 * @param cityDO
	 * @return 记录数
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 14:58:14
	 */
	Long selectCountDynamic(CityDO cityDO) throws ServiceException;

	/**
	 * 动态返回 列表
	 * 
	 * @param cityDO
	 * @return List<CityDO>
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 14:58:14
	 */
	List<CityDO> selectDynamic(CityDO cityDO) throws ServiceException;

	/**
	 * 动态返回 分页列表
	 * 
	 * @param cityDO
	 * @return Page<CityDO>
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 14:58:14
	 */
	Page<CityDO> queryPageListByCityDO(CityDO cityDO);

	/**
	 * 动态返回 分页列表
	 * 
	 * @param cityDO
	 * @param startPage
	 *            起始页
	 * @param pageSize
	 *            每页记录数
	 * @return Page<CityDO>
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 14:58:14
	 */
	Page<CityDO> queryPageListByCityDOAndStartPageSize(CityDO cityDO, int startPage, int pageSize);

}
