package com.elsa.configserver.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.elsa.configserver.dao.AppNameDAO;
import com.elsa.configserver.dao.CityDAO;
import com.elsa.configserver.domain.AppNameDO;
import com.elsa.configserver.domain.CityDO;
import com.elsa.configserver.exception.DAOException;
import com.elsa.configserver.exception.ServiceException;

public abstract class AbstractCityService {

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	CityDAO cityDAO;

	@Autowired
	AppNameDAO appNameDAO;

}
