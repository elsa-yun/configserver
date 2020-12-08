package com.elsa.configserver.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.elsa.configserver.dao.UsersDAO;
import com.elsa.configserver.domain.UsersDO;
import com.elsa.configserver.exception.DAOException;
import com.elsa.configserver.exception.ServiceException;
import com.elsa.configserver.service.UsersService;
import com.elsa.configserver.util.Page;

@Service(value = "usersService")
public class UsersServiceImpl implements UsersService {

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private UsersDAO usersDAO;

	public Long insert(UsersDO usersDO) throws ServiceException {
		try {
			return usersDAO.insert(usersDO);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	//
	// public int updateById(UsersDO usersDO) throws ServiceException {
	// try {
	// return (Integer) usersDAO.updateById(usersDO);
	// }catch(DAOException e){
	// logger.error(e);
	// throw new ServiceException(e);
	// }
	// }

	public int update(UsersDO usersDO, boolean isAllField) throws ServiceException {
		try {
			if (isAllField) {
				return (Integer) usersDAO.update(usersDO);
			} else {
				return (Integer) usersDAO.updateDynamic(usersDO);
			}
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	public int deleteById(Long id) throws ServiceException {
		try {
			return (Integer) usersDAO.deleteById(id);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	//
	// public int updateDynamic(UsersDO usersDO) throws ServiceException {
	// try {
	// return (Integer) usersDAO.updateDynamic(usersDO);
	// }catch(DAOException e){
	// logger.error(e);
	// throw new ServiceException(e);
	// }
	// }

	public UsersDO selectById(Long id) throws ServiceException {
		try {
			return usersDAO.selectById(id);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	public Long selectCountDynamic(UsersDO usersDO) throws ServiceException {
		try {
			return usersDAO.selectCountDynamic(usersDO);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	public List<UsersDO> selectDynamic(UsersDO usersDO) throws ServiceException {
		try {
			return usersDAO.selectDynamic(usersDO);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	private List<UsersDO> selectDynamicPageQuery(UsersDO usersDO) throws ServiceException {
		try {
			return usersDAO.selectDynamicPageQuery(usersDO);
		} catch (DAOException e) {
			logger.error(e);
			throw new ServiceException(e);
		}
	}

	public Page<UsersDO> queryPageListByUsersDO(UsersDO usersDO) {
		if (usersDO != null) {
			Long totalCount = this.selectCountDynamic(usersDO);
			List<UsersDO> resultList = this.selectDynamicPageQuery(usersDO);

			Page<UsersDO> page = new Page<UsersDO>();
			page.setPageNo(usersDO.getStartPage());
			page.setPageSize(usersDO.getPageSize());
			page.setTotalCount(totalCount.intValue());
			page.setList(resultList);
			return page;
		}
		return new Page<UsersDO>();
	}

	public Page<UsersDO> queryPageListByUsersDOAndStartPageSize(UsersDO usersDO, int startPage, int pageSize) {
		if (usersDO != null && startPage > 0 && pageSize > 0) {
			usersDO.setStartPage(startPage);
			usersDO.setPageSize(pageSize);
			return this.queryPageListByUsersDO(usersDO);
		}
		return new Page<UsersDO>();
	}

}
