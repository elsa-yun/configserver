package com.elsa.configserver.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.elsa.configserver.dao.OpraterLogDAO;
import com.elsa.configserver.domain.OpraterLogDO;
import com.elsa.configserver.exception.DAOException;
import com.elsa.configserver.exception.ServiceException;
import com.elsa.configserver.service.OpraterLogService;
import com.elsa.configserver.util.Page;

@Service(value = "opraterLogService")
public class OpraterLogServiceImpl implements OpraterLogService {

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private OpraterLogDAO opraterLogDAO;

	public Long insert(OpraterLogDO opraterLogDO) throws ServiceException {
		try {
			return opraterLogDAO.insert(opraterLogDO);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	//
	// public int updateById(OpraterLogDO opraterLogDO) throws ServiceException
	// {
	// try {
	// return (Integer) opraterLogDAO.updateById(opraterLogDO);
	// }catch(DAOException e){
	// logger.error(e);
	// throw new ServiceException(e);
	// }
	// }

	public int update(OpraterLogDO opraterLogDO, boolean isAllField) throws ServiceException {
		try {
			if (isAllField) {
				return (Integer) opraterLogDAO.update(opraterLogDO);
			} else {
				return (Integer) opraterLogDAO.updateDynamic(opraterLogDO);
			}
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	public int deleteById(Long id) throws ServiceException {
		try {
			return (Integer) opraterLogDAO.deleteById(id);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	//
	// public int updateDynamic(OpraterLogDO opraterLogDO) throws
	// ServiceException {
	// try {
	// return (Integer) opraterLogDAO.updateDynamic(opraterLogDO);
	// }catch(DAOException e){
	// logger.error(e);
	// throw new ServiceException(e);
	// }
	// }

	public OpraterLogDO selectById(Long id) throws ServiceException {
		try {
			return opraterLogDAO.selectById(id);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	public Long selectCountDynamic(OpraterLogDO opraterLogDO) throws ServiceException {
		try {
			return opraterLogDAO.selectCountDynamic(opraterLogDO);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	public List<OpraterLogDO> selectDynamic(OpraterLogDO opraterLogDO) throws ServiceException {
		try {
			return opraterLogDAO.selectDynamic(opraterLogDO);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	private List<OpraterLogDO> selectDynamicPageQuery(OpraterLogDO opraterLogDO) throws ServiceException {
		try {
			return opraterLogDAO.selectDynamicPageQuery(opraterLogDO);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	public Page<OpraterLogDO> queryPageListByOpraterLogDO(OpraterLogDO opraterLogDO) {
		if (opraterLogDO != null) {
			Long totalCount = this.selectCountDynamic(opraterLogDO);
			List<OpraterLogDO> resultList = this.selectDynamicPageQuery(opraterLogDO);

			Page<OpraterLogDO> page = new Page<OpraterLogDO>();
			page.setPageNo(opraterLogDO.getStartPage());
			page.setPageSize(opraterLogDO.getPageSize());
			page.setTotalCount(totalCount.intValue());
			page.setList(resultList);
			return page;
		}
		return new Page<OpraterLogDO>();
	}

	public Page<OpraterLogDO> queryPageListByOpraterLogDOAndStartPageSize(OpraterLogDO opraterLogDO, int startPage, int pageSize) {
		if (opraterLogDO != null && startPage > 0 && pageSize > 0) {
			opraterLogDO.setStartPage(startPage);
			opraterLogDO.setPageSize(pageSize);
			return this.queryPageListByOpraterLogDO(opraterLogDO);
		}
		return new Page<OpraterLogDO>();
	}

}
