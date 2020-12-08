package com.elsa.configserver.dao.mybatis;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

import com.elsa.configserver.dao.OpraterLogDAO;
import com.elsa.configserver.domain.OpraterLogDO;
import com.elsa.configserver.exception.DAOException;

@Component(value = "opraterLogDAO")
public class MybatisOpraterLogDAO extends MybatisBaseDAO implements OpraterLogDAO {
	public Long insert(OpraterLogDO opraterLogDO) throws DAOException {
		int i = getSqlSession().insert("com.elsa.configserver.domain.OpraterLogMapper.MybatisOpraterLogDAO_insert", opraterLogDO);
		if (i > 0) {
			return Long.valueOf(opraterLogDO.getId());
		}
		return 0L;
	}

	public Integer update(OpraterLogDO opraterLogDO) throws DAOException {
		return getSqlSession().update("com.elsa.configserver.domain.OpraterLogMapper.MybatisOpraterLogDAO_updateById", opraterLogDO);
	}

	public Integer deleteById(Long id) throws DAOException {
		return getSqlSession().delete("com.elsa.configserver.domain.OpraterLogMapper.MybatisOpraterLogDAO_deleteById", id);
	}

	public Integer updateDynamic(OpraterLogDO opraterLogDO) throws DAOException {
		return getSqlSession().update("com.elsa.configserver.domain.OpraterLogMapper.MybatisOpraterLogDAO_update_dynamic", opraterLogDO);
	}

	public OpraterLogDO selectById(Long id) throws DAOException {
		return getSqlSession().selectOne("com.elsa.configserver.domain.OpraterLogMapper.MybatisOpraterLogDAO_selectById", id);
	}

	public Long selectCountDynamic(OpraterLogDO opraterLogDO) throws DAOException {
		return getSqlSession().selectOne("com.elsa.configserver.domain.OpraterLogMapper.MybatisOpraterLogDAO_select_dynamic_count",
				opraterLogDO);
	}

	public List<OpraterLogDO> selectDynamic(OpraterLogDO opraterLogDO) throws DAOException {
		return getSqlSession().selectList("com.elsa.configserver.domain.OpraterLogMapper.MybatisOpraterLogDAO_select_dynamic",
				opraterLogDO);
	}

	public List<OpraterLogDO> selectDynamicPageQuery(OpraterLogDO opraterLogDO) throws DAOException {
		return getSqlSession().selectList("com.elsa.configserver.domain.OpraterLogMapper.MybatisOpraterLogDAO_select_dynamic_page_query",
				opraterLogDO);
	}

}
