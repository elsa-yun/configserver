package com.elsa.configserver.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.elsa.configserver.dao.AppNameDAO;
import com.elsa.configserver.domain.AppNameDO;
import com.elsa.configserver.exception.DAOException;
import com.elsa.configserver.exception.ServiceException;
import com.elsa.configserver.service.AppNameService;
import com.elsa.configserver.util.Page;

@Service(value = "appNameService")
public class AppNameServiceImpl implements AppNameService {

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private AppNameDAO appNameDAO;

	public Long insert(AppNameDO appNameDO) throws ServiceException {
		try {
			return appNameDAO.insert(appNameDO);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	//
	// public int updateById(AppNameDO appNameDO) throws ServiceException {
	// try {
	// return (Integer) appNameDAO.updateById(appNameDO);
	// }catch(DAOException e){
	// logger.error(e);
	// throw new ServiceException(e);
	// }
	// }

	public int update(AppNameDO appNameDO, boolean isAllField) throws ServiceException {
		try {
			if (isAllField) {
				return (Integer) appNameDAO.update(appNameDO);
			} else {
				return (Integer) appNameDAO.updateDynamic(appNameDO);
			}
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	public int deleteById(Long id) throws ServiceException {
		try {
			return (Integer) appNameDAO.deleteById(id);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	//
	// public int updateDynamic(AppNameDO appNameDO) throws ServiceException {
	// try {
	// return (Integer) appNameDAO.updateDynamic(appNameDO);
	// }catch(DAOException e){
	// logger.error(e);
	// throw new ServiceException(e);
	// }
	// }

	public AppNameDO selectById(Long id) throws ServiceException {
		try {
			return appNameDAO.selectById(id);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	public Long selectCountDynamic(AppNameDO appNameDO) throws ServiceException {
		try {
			return appNameDAO.selectCountDynamic(appNameDO);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	public List<AppNameDO> selectDynamic(AppNameDO appNameDO) throws ServiceException {
		try {
			return appNameDAO.selectDynamic(appNameDO);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	private List<AppNameDO> selectDynamicPageQuery(AppNameDO appNameDO) throws ServiceException {
		try {
			return appNameDAO.selectDynamicPageQuery(appNameDO);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	public Page<AppNameDO> queryPageListByAppNameDO(AppNameDO appNameDO) {
		if (appNameDO != null) {
			Long totalCount = this.selectCountDynamic(appNameDO);
			List<AppNameDO> resultList = this.selectDynamicPageQuery(appNameDO);

			Page<AppNameDO> page = new Page<AppNameDO>();
			page.setPageNo(appNameDO.getStartPage());
			page.setPageSize(appNameDO.getPageSize());
			page.setTotalCount(totalCount.intValue());
			page.setList(resultList);
			return page;
		}
		return new Page<AppNameDO>();
	}

	public Page<AppNameDO> queryPageListByAppNameDOAndStartPageSize(AppNameDO appNameDO, int startPage, int pageSize) {
		if (appNameDO != null && startPage > 0 && pageSize > 0) {
			appNameDO.setStartPage(startPage);
			appNameDO.setPageSize(pageSize);
			return this.queryPageListByAppNameDO(appNameDO);
		}
		return new Page<AppNameDO>();
	}

}
