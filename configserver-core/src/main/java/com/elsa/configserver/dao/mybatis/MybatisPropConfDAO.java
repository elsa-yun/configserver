package com.elsa.configserver.dao.mybatis;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

import com.elsa.configserver.dao.PropConfDAO;
import com.elsa.configserver.domain.PropConfDO;
import com.elsa.configserver.exception.DAOException;

@Component(value = "propConfDAO")
public class MybatisPropConfDAO extends MybatisBaseDAO implements PropConfDAO {
	public Long insert(PropConfDO propConfDO) throws DAOException {
		int i = getSqlSession().insert("com.elsa.configserver.domain.PropConfMapper.MybatisPropConfDAO_insert", propConfDO);
		if (i > 0) {
			return Long.valueOf(propConfDO.getId());
		}
		return 0L;
	}

	public Integer update(PropConfDO propConfDO) throws DAOException {
		return getSqlSession().update("com.elsa.configserver.domain.PropConfMapper.MybatisPropConfDAO_updateById", propConfDO);
	}

	public Integer deleteById(Long id) throws DAOException {
		return getSqlSession().delete("com.elsa.configserver.domain.PropConfMapper.MybatisPropConfDAO_deleteById", id);
	}

	public Integer updateDynamic(PropConfDO propConfDO) throws DAOException {
		return getSqlSession().update("com.elsa.configserver.domain.PropConfMapper.MybatisPropConfDAO_update_dynamic", propConfDO);
	}

	public PropConfDO selectById(Long id) throws DAOException {
		return getSqlSession().selectOne("com.elsa.configserver.domain.PropConfMapper.MybatisPropConfDAO_selectById", id);
	}

	public Long selectCountDynamic(PropConfDO propConfDO) throws DAOException {
		return getSqlSession().selectOne("com.elsa.configserver.domain.PropConfMapper.MybatisPropConfDAO_select_dynamic_count",
				propConfDO);
	}

	public List<PropConfDO> selectDynamic(PropConfDO propConfDO) throws DAOException {
		return getSqlSession().selectList("com.elsa.configserver.domain.PropConfMapper.MybatisPropConfDAO_select_dynamic", propConfDO);
	}

	public List<PropConfDO> selectDynamicPageQuery(PropConfDO propConfDO) throws DAOException {
		return getSqlSession().selectList("com.elsa.configserver.domain.PropConfMapper.MybatisPropConfDAO_select_dynamic_page_query",
				propConfDO);
	}

	public PropConfDO selectOne(PropConfDO propConfDO) throws DAOException {
		return getSqlSession().selectOne("com.elsa.configserver.domain.PropConfMapper.MybatisPropConfDAO_select_one_with_propconf",
				propConfDO);
	}

}
