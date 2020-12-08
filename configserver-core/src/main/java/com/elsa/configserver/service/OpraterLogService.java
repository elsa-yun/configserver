package com.elsa.configserver.service;

import java.util.List;

import com.elsa.configserver.domain.OpraterLogDO;
import com.elsa.configserver.exception.ServiceException;
import com.elsa.configserver.util.Page;

/**
 * 操作日志 Service
 * 
 * @author haisheng.long 2015-01-28 18:51:19
 */
public interface OpraterLogService {

	/**
	 * 插入 操作日志
	 * 
	 * @param opraterLogDO
	 * @return 主键
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Long insert(OpraterLogDO opraterLogDO) throws ServiceException;

	/**
	 * 根据OpraterLogDO对象更新 操作日志
	 * 
	 * @param opraterLogDO
	 * @param isAllField
	 *            是否更新所有字段(false 动态更新字段，true 更新所有字段,传null或false将动态更新，建议动态更新)
	 * @return 更新行数
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	int update(OpraterLogDO opraterLogDO, boolean isAllField) throws ServiceException;

	// /**
	// * 根据ID更新 操作日志全部字段
	// * @param opraterLogDO
	// * @return 更新行数
	// * @throws ServiceException
	// * @author longhaisheng 2015-01-28 18:51:19
	// */
	// int updateById(OpraterLogDO opraterLogDO) throws ServiceException;

	/**
	 * 根据ID删除 操作日志
	 * 
	 * @param id
	 * @return 物理删除行
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	int deleteById(Long id) throws ServiceException;

	// /**
	// * 动态更新 操作日志部分字段
	// * @param opraterLogDO
	// * @return 更新行数
	// * @throws ServiceException
	// * @author longhaisheng 2015-01-28 18:51:19
	// */
	// int updateDynamic(OpraterLogDO opraterLogDO) throws ServiceException;

	/**
	 * 根据ID查询 一个 操作日志
	 * 
	 * @param id
	 * @return OpraterLogDO
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	OpraterLogDO selectById(Long id) throws ServiceException;

	/**
	 * 根据 操作日志 动态返回记录数
	 * 
	 * @param opraterLogDO
	 * @return 记录数
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Long selectCountDynamic(OpraterLogDO opraterLogDO) throws ServiceException;

	/**
	 * 动态返回 操作日志 列表
	 * 
	 * @param opraterLogDO
	 * @return List<OpraterLogDO>
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	List<OpraterLogDO> selectDynamic(OpraterLogDO opraterLogDO) throws ServiceException;

	/**
	 * 动态返回 操作日志 分页列表
	 * 
	 * @param opraterLogDO
	 * @return Page<OpraterLogDO>
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Page<OpraterLogDO> queryPageListByOpraterLogDO(OpraterLogDO opraterLogDO);

	/**
	 * 动态返回 操作日志 分页列表
	 * 
	 * @param opraterLogDO
	 * @param startPage
	 *            起始页
	 * @param pageSize
	 *            每页记录数
	 * @return Page<OpraterLogDO>
	 * @throws ServiceException
	 * @author longhaisheng 2015-01-28 18:51:19
	 */
	Page<OpraterLogDO> queryPageListByOpraterLogDOAndStartPageSize(OpraterLogDO opraterLogDO, int startPage, int pageSize);

}
