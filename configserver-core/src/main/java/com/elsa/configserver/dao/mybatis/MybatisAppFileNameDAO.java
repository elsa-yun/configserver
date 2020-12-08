package com.elsa.configserver.dao.mybatis;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

import com.elsa.configserver.dao.AppFileNameDAO;
import com.elsa.configserver.domain.AppFileNameDO;
import com.elsa.configserver.exception.DAOException;

@Component(value = "appFileNameDAO")
public class MybatisAppFileNameDAO extends MybatisBaseDAO implements AppFileNameDAO {
	public Long insert(AppFileNameDO appFileNameDO) throws DAOException {
		int i = getSqlSession().insert("com.elsa.configserver.domain.AppFileNameMapper.MybatisAppFileNameDAO_insert", appFileNameDO);
		if (i > 0) {
			return Long.valueOf(appFileNameDO.getId());
		}
		return 0L;
	}

	@Override
	public Integer update(AppFileNameDO appFileNameDO) throws DAOException {
		return getSqlSession().update("com.elsa.configserver.domain.AppFileNameMapper.MybatisAppFileNameDAO_updateById", appFileNameDO);
	}

	@Override
	public Integer deleteById(Long id) throws DAOException {
		return getSqlSession().delete("com.elsa.configserver.domain.AppFileNameMapper.MybatisAppFileNameDAO_deleteById", id);
	}

	@Override
	public Integer updateDynamic(AppFileNameDO appFileNameDO) throws DAOException {
		return getSqlSession().update("com.elsa.configserver.domain.AppFileNameMapper.MybatisAppFileNameDAO_update_dynamic",
				appFileNameDO);
	}

	@Override
	public AppFileNameDO selectById(Long id) throws DAOException {
		return getSqlSession().selectOne("com.elsa.configserver.domain.AppFileNameMapper.MybatisAppFileNameDAO_selectById", id);
	}

	@Override
	public Long selectCountDynamic(AppFileNameDO appFileNameDO) throws DAOException {
		return getSqlSession().selectOne("com.elsa.configserver.domain.AppFileNameMapper.MybatisAppFileNameDAO_select_dynamic_count",
				appFileNameDO);
	}

	@Override
	public List<AppFileNameDO> selectDynamic(AppFileNameDO appFileNameDO) throws DAOException {
		return getSqlSession().selectList("com.elsa.configserver.domain.AppFileNameMapper.MybatisAppFileNameDAO_select_dynamic",
				appFileNameDO);
	}

	@Override
	public List<AppFileNameDO> selectDynamicPageQuery(AppFileNameDO appFileNameDO) throws DAOException {
		return getSqlSession().selectList(
				"com.elsa.configserver.domain.AppFileNameMapper.MybatisAppFileNameDAO_select_dynamic_page_query", appFileNameDO);
	}

	@Override
	public List<AppFileNameDO> queryByAppIds(List<Integer> appIds) throws DAOException {
		return getSqlSession().selectList("com.elsa.configserver.domain.AppFileNameMapper.MybatisAppFileNameDAO_select_by_app_ids",
				appIds);
	}

}
