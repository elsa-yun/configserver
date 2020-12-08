package com.elsa.configserver.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.elsa.configserver.dao.PropConfDAO;
import com.elsa.configserver.domain.PropConfDO;
import com.elsa.configserver.exception.DAOException;
import com.elsa.configserver.exception.ServiceException;
import com.elsa.configserver.service.PropConfService;
import com.elsa.configserver.util.Page;

@Service(value = "propConfService")
public class PropConfServiceImpl implements PropConfService {

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private PropConfDAO propConfDAO;

	public Long insert(PropConfDO propConfDO) throws ServiceException {
		try {
			return propConfDAO.insert(propConfDO);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	//
	// public int updateById(PropConfDO propConfDO) throws ServiceException {
	// try {
	// return (Integer) propConfDAO.updateById(propConfDO);
	// }catch(DAOException e){
	// logger.error(e);
	// throw new ServiceException(e);
	// }
	// }

	public int update(PropConfDO propConfDO, boolean isAllField) throws ServiceException {
		try {
			if (isAllField) {
				return (Integer) propConfDAO.update(propConfDO);
			} else {
				return (Integer) propConfDAO.updateDynamic(propConfDO);
			}
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	public int deleteById(Long id) throws ServiceException {
		try {
			return (Integer) propConfDAO.deleteById(id);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	//
	// public int updateDynamic(PropConfDO propConfDO) throws ServiceException {
	// try {
	// return (Integer) propConfDAO.updateDynamic(propConfDO);
	// }catch(DAOException e){
	// logger.error(e);
	// throw new ServiceException(e);
	// }
	// }

	public PropConfDO selectById(Long id) throws ServiceException {
		try {
			return propConfDAO.selectById(id);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	public Long selectCountDynamic(PropConfDO propConfDO) throws ServiceException {
		try {
			return propConfDAO.selectCountDynamic(propConfDO);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	public List<PropConfDO> selectDynamic(PropConfDO propConfDO) throws ServiceException {
		try {
			return propConfDAO.selectDynamic(propConfDO);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	private List<PropConfDO> selectDynamicPageQuery(PropConfDO propConfDO) throws ServiceException {
		try {
			return propConfDAO.selectDynamicPageQuery(propConfDO);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	public Page<PropConfDO> queryPageListByPropConfDO(PropConfDO propConfDO) {
		if (propConfDO != null) {
			Long totalCount = this.selectCountDynamic(propConfDO);
			List<PropConfDO> resultList = this.selectDynamicPageQuery(propConfDO);

			Page<PropConfDO> page = new Page<PropConfDO>();
			page.setPageNo(propConfDO.getStartPage());
			page.setPageSize(propConfDO.getPageSize());
			page.setTotalCount(totalCount.intValue());
			page.setList(resultList);
			return page;
		}
		return new Page<PropConfDO>();
	}

	public Page<PropConfDO> queryPageListByPropConfDOAndStartPageSize(PropConfDO propConfDO, int startPage, int pageSize) {
		if (propConfDO != null && startPage > 0 && pageSize > 0) {
			propConfDO.setStartPage(startPage);
			propConfDO.setPageSize(pageSize);
			return this.queryPageListByPropConfDO(propConfDO);
		}
		return new Page<PropConfDO>();
	}

	public PropConfDO selectOne(PropConfDO propConfDO) throws ServiceException {
		try {
			return propConfDAO.selectOne(propConfDO);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

}
