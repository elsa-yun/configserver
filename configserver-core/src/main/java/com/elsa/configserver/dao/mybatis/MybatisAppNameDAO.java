package com.elsa.configserver.dao.mybatis;

import java.util.List;

import org.springframework.stereotype.Component;

import com.elsa.configserver.dao.AppNameDAO;
import com.elsa.configserver.domain.AppNameDO;
import com.elsa.configserver.exception.DAOException;

@Component(value = "appNameDAO")
public class MybatisAppNameDAO extends MybatisBaseDAO implements AppNameDAO {
	public Long insert(AppNameDO appNameDO) throws DAOException {
		int i = getSqlSession().insert("com.elsa.configserver.domain.AppNameMapper.MybatisAppNameDAO_insert", appNameDO);
		if (i > 0) {
			return Long.valueOf(appNameDO.getId());
		}
		return 0L;
	}

	public Integer update(AppNameDO appNameDO) throws DAOException {
		return getSqlSession().update("com.elsa.configserver.domain.AppNameMapper.MybatisAppNameDAO_updateById", appNameDO);
	}

	public Integer deleteById(Long id) throws DAOException {
		return getSqlSession().delete("com.elsa.configserver.domain.AppNameMapper.MybatisAppNameDAO_deleteById", id);
	}

	public Integer updateDynamic(AppNameDO appNameDO) throws DAOException {
		return getSqlSession().update("com.elsa.configserver.domain.AppNameMapper.MybatisAppNameDAO_update_dynamic", appNameDO);
	}

	public AppNameDO selectById(Long id) throws DAOException {
		return getSqlSession().selectOne("com.elsa.configserver.domain.AppNameMapper.MybatisAppNameDAO_selectById", id);
	}

	public Long selectCountDynamic(AppNameDO appNameDO) throws DAOException {
		return getSqlSession().selectOne("com.elsa.configserver.domain.AppNameMapper.MybatisAppNameDAO_select_dynamic_count", appNameDO);
	}

	public List<AppNameDO> selectDynamic(AppNameDO appNameDO) throws DAOException {
		return getSqlSession().selectList("com.elsa.configserver.domain.AppNameMapper.MybatisAppNameDAO_select_dynamic", appNameDO);
	}

	public List<AppNameDO> selectDynamicPageQuery(AppNameDO appNameDO) throws DAOException {
		return getSqlSession().selectList("com.elsa.configserver.domain.AppNameMapper.MybatisAppNameDAO_select_dynamic_page_query",
				appNameDO);
	}

}
