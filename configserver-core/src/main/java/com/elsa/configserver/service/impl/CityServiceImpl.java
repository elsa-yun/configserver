package com.elsa.configserver.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.elsa.configserver.dao.AppNameDAO;
import com.elsa.configserver.dao.CityDAO;
import com.elsa.configserver.domain.AppNameDO;
import com.elsa.configserver.domain.CityDO;
import com.elsa.configserver.exception.DAOException;
import com.elsa.configserver.exception.ServiceException;
import com.elsa.configserver.service.CityService;
import com.elsa.configserver.util.Page;

@Service(value = "cityService")
public class CityServiceImpl implements CityService {

	@Autowired
	CityDAO cityDAO;

	@Autowired
	AppNameDAO appNameDAO;

	@Autowired
	TransactionTemplate transactionTemplate;

	private Log logger = LogFactory.getLog(this.getClass());

	public Long insert(CityDO cityDO) throws ServiceException {
		try {
			return cityDAO.insert(cityDO);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	// @Override
	// public int updateById(CityDO cityDO) throws ServiceException {
	// try {
	// return (Integer) cityDAO.updateById(cityDO);
	// }catch(DAOException e){
	// logger.error(e);
	// throw new ServiceException(e);
	// }
	// }

	public int update(CityDO cityDO, boolean isAllField) throws ServiceException {
		try {
			if (isAllField) {
				return (Integer) cityDAO.update(cityDO);
			} else {
				return (Integer) cityDAO.updateDynamic(cityDO);
			}
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	public int deleteById(Long id) throws ServiceException {
		try {
			return (Integer) cityDAO.deleteById(id);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	// @Override
	// public int updateDynamic(CityDO cityDO) throws ServiceException {
	// try {
	// return (Integer) cityDAO.updateDynamic(cityDO);
	// }catch(DAOException e){
	// logger.error(e);
	// throw new ServiceException(e);
	// }
	// }

	public CityDO selectById(final int id, final int newParam) throws ServiceException {
		// try {
		// final CityDO cityDO = cityDAO.selectById(Long.valueOf(id), true);
		// logger.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		// transactionTemplate.execute(new TransactionCallback<Object>() {
		// public Object doInTransaction(TransactionStatus status) {
		// cityDO.setId(1);
		// cityDO.setName("haha" + newParam);
		// try {
		// t_update(cityDO);
		// CityDO city = cityDAO.selectById(Long.valueOf(id), true);
		// AppNameDO appNameDO = appNameDAO.selectById(7L);
		// appNameDO.setAppName("orderweb");
		// appNameDAO.update(appNameDO);
		// appNameDO = appNameDAO.selectById(8L);
		// appNameDO.setAppName("orderweb");
		// appNameDAO.update(appNameDO);
		// } catch (DAOException e) {
		// e.printStackTrace();
		// }
		// return status;
		// }
		//
		// });
		// logger.debug("=====================33333333=========================>  cityDAO.selectById(id, false) "
		// + cityDO.toString());
		// logger.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		// return cityDO;
		// } catch (DAOException e) {
		// logger.error(e);
		// throw new ServiceException(e);
		// }
		return null;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public CityDO transactional(final int id, final int newParam) throws ServiceException {
		// try {
		// final CityDO cityDO = cityDAO.selectById(Long.valueOf(id), true);
		// logger.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@transactional@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		// cityDO.setId(1);
		// cityDO.setName("haha" + newParam);
		// try {
		// t_update(cityDO);
		// CityDO city = cityDAO.selectById(Long.valueOf(id), true);
		// AppNameDO appNameDO = appNameDAO.selectById(7L);
		// appNameDO.setAppName("orderweb");
		// appNameDAO.update(appNameDO);
		// appNameDO = appNameDAO.selectById(8L);
		// appNameDO.setAppName("orderweb");
		// appNameDAO.update(appNameDO);
		// } catch (DAOException e) {
		// e.printStackTrace();
		// }
		//
		// logger.debug("=====================33333333=========================>  cityDAO.selectById(id, false) "
		// + cityDO.toString());
		// logger.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@transactional@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		// return cityDO;
		// } catch (DAOException e) {
		// logger.error(e);
		// throw new ServiceException(e);
		// }
		return null;
	}

	private void t_update(CityDO cityDO) throws DAOException {
		logger.debug("==================11111111============================> cityDAO.selectById(id, true)");
		cityDO.setSpelling(cityDO.getName());
		cityDAO.updateDynamic(cityDO);
		logger.debug("=====================222222222=========================> cityDAO.update(cityDO)");
	}

	@Transactional
	public CityDO select(int id) throws ServiceException {
		try {
			logger.debug("======================last 4444444444444444========================>");
			CityDO cityDO = cityDAO.selectById(Long.valueOf(id), true);
			try {
				logger.debug("getSpelling:::::::::>>>>"+cityDO.getSpelling());
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			logger.debug("======================last 44444444444444========================> cityDAO.selectById(id, true)");
			return cityDO;
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	public Long selectCountDynamic(CityDO cityDO) throws ServiceException {
		try {
			return cityDAO.selectCountDynamic(cityDO);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	public List<CityDO> selectDynamic(CityDO cityDO) throws ServiceException {
		try {
			return cityDAO.selectDynamic(cityDO);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	private List<CityDO> selectDynamicPageQuery(CityDO cityDO) throws ServiceException {
		try {
			return cityDAO.selectDynamicPageQuery(cityDO);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	public Page<CityDO> queryPageListByCityDO(CityDO cityDO) {
		if (cityDO != null) {
			Long totalCount = this.selectCountDynamic(cityDO);
			List<CityDO> resultList = this.selectDynamicPageQuery(cityDO);

			Page<CityDO> page = new Page<CityDO>();
			page.setPageNo(cityDO.getStartPage());
			page.setPageSize(cityDO.getPageSize());
			page.setTotalCount(totalCount.intValue());
			page.setList(resultList);
			return page;
		}
		return new Page<CityDO>();
	}

	public Page<CityDO> queryPageListByCityDOAndStartPageSize(CityDO cityDO, int startPage, int pageSize) {
		if (cityDO != null && startPage > 0 && pageSize > 0) {
			cityDO.setStartPage(startPage);
			cityDO.setPageSize(pageSize);
			return this.queryPageListByCityDO(cityDO);
		}
		return new Page<CityDO>();
	}

}
