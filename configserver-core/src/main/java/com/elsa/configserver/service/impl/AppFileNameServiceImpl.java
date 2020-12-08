package com.elsa.configserver.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.elsa.configserver.dao.AppFileNameDAO;
import com.elsa.configserver.domain.AppFileNameDO;
import com.elsa.configserver.exception.DAOException;
import com.elsa.configserver.exception.ServiceException;
import com.elsa.configserver.service.AppFileNameService;
import com.elsa.configserver.util.Page;

@Service(value = "appFileNameService")
public class AppFileNameServiceImpl implements AppFileNameService {

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private AppFileNameDAO appFileNameDAO;

	@Override
	public Long insert(AppFileNameDO appFileNameDO) throws ServiceException {
		try {
			return appFileNameDAO.insert(appFileNameDO);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	// @Override
	// public int updateById(AppFileNameDO appFileNameDO) throws
	// ServiceException {
	// try {
	// return (Integer) appFileNameDAO.updateById(appFileNameDO);
	// }catch(DAOException e){
	// logger.error(e);
	// throw new ServiceException(e);
	// }
	// }

	@Override
	public int update(AppFileNameDO appFileNameDO, boolean isAllField) throws ServiceException {
		try {
			if (isAllField) {
				return (Integer) appFileNameDAO.update(appFileNameDO);
			} else {
				return (Integer) appFileNameDAO.updateDynamic(appFileNameDO);
			}
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	@Override
	public int deleteById(Long id) throws ServiceException {
		try {
			return (Integer) appFileNameDAO.deleteById(id);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	// @Override
	// public int updateDynamic(AppFileNameDO appFileNameDO) throws
	// ServiceException {
	// try {
	// return (Integer) appFileNameDAO.updateDynamic(appFileNameDO);
	// }catch(DAOException e){
	// logger.error(e);
	// throw new ServiceException(e);
	// }
	// }

	@Override
	public AppFileNameDO selectById(Long id) throws ServiceException {
		try {
			return appFileNameDAO.selectById(id);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	@Override
	public Long selectCountDynamic(AppFileNameDO appFileNameDO) throws ServiceException {
		try {
			return appFileNameDAO.selectCountDynamic(appFileNameDO);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	@Override
	public List<AppFileNameDO> selectDynamic(AppFileNameDO appFileNameDO) throws ServiceException {
		try {
			return appFileNameDAO.selectDynamic(appFileNameDO);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	private List<AppFileNameDO> selectDynamicPageQuery(AppFileNameDO appFileNameDO) throws ServiceException {
		try {
			return appFileNameDAO.selectDynamicPageQuery(appFileNameDO);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	@Override
	public List<AppFileNameDO> queryByAppIds(List<Integer> appIds) throws ServiceException {
		try {
			return appFileNameDAO.queryByAppIds(appIds);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	public Page<AppFileNameDO> queryPageListByAppFileNameDO(AppFileNameDO appFileNameDO) {
		if (appFileNameDO != null) {
			Long totalCount = this.selectCountDynamic(appFileNameDO);
			List<AppFileNameDO> resultList = this.selectDynamicPageQuery(appFileNameDO);

			Page<AppFileNameDO> page = new Page<AppFileNameDO>();
			page.setPageNo(appFileNameDO.getStartPage());
			page.setPageSize(appFileNameDO.getPageSize());
			page.setTotalCount(totalCount.intValue());
			page.setList(resultList);
			return page;
		}
		return new Page<AppFileNameDO>();
	}

	public Page<AppFileNameDO> queryPageListByAppFileNameDOAndStartPageSize(AppFileNameDO appFileNameDO, int startPage, int pageSize) {
		if (appFileNameDO != null && startPage > 0 && pageSize > 0) {
			appFileNameDO.setStartPage(startPage);
			appFileNameDO.setPageSize(pageSize);
			return this.queryPageListByAppFileNameDO(appFileNameDO);
		}
		return new Page<AppFileNameDO>();
	}

}
