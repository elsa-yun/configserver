package com.elsa.configserver.dao.mybatis;

import java.util.List;

import org.springframework.stereotype.Component;

import com.elsa.configserver.dao.CityDAO;
import com.elsa.configserver.domain.CityDO;
import com.elsa.configserver.exception.DAOException;
import com.elsa.seller.datasource.ContextHolder;

@Component(value = "cityDAO")
public class MybatisCityDAO extends MybatisBaseDAO implements CityDAO {
	public Long insert(CityDO cityDO) throws DAOException {
		int i = getSqlSession().insert("com.elsa.configserver.domain.CityMapper.MybatisCityDAO_insert", cityDO);
		if (i > 0) {
			return Long.valueOf(cityDO.getId());
		}
		return 0L;
	}

	public Integer update(CityDO cityDO) throws DAOException {
		return getSqlSession().update("com.elsa.configserver.domain.CityMapper.MybatisCityDAO_updateById", cityDO);
	}

	public Integer deleteById(Long id) throws DAOException {
		return getSqlSession().delete("com.elsa.configserver.domain.CityMapper.MybatisCityDAO_deleteById", id);
	}

	public Integer updateDynamic(CityDO cityDO) throws DAOException {
		return getSqlSession().update("com.elsa.configserver.domain.CityMapper.MybatisCityDAO_update_dynamic", cityDO);
	}

	public CityDO selectById(Long id, boolean readSlave) throws DAOException {
		if (readSlave) {
			ContextHolder.setCustomerKey(ContextHolder.SLAVE_ORDER_DATE_SOURCE);
		}
		return getSqlSession().selectOne("com.elsa.configserver.domain.CityMapper.MybatisCityDAO_selectById", id);
	}

	public Long selectCountDynamic(CityDO cityDO) throws DAOException {
		return getSqlSession().selectOne("com.elsa.configserver.domain.CityMapper.MybatisCityDAO_select_dynamic_count", cityDO);
	}

	public List<CityDO> selectDynamic(CityDO cityDO) throws DAOException {
		return getSqlSession().selectList("com.elsa.configserver.domain.CityMapper.MybatisCityDAO_select_dynamic", cityDO);
	}

	public List<CityDO> selectDynamicPageQuery(CityDO cityDO) throws DAOException {
		return getSqlSession().selectList("com.elsa.configserver.domain.CityMapper.MybatisCityDAO_select_dynamic_page_query", cityDO);
	}

}
