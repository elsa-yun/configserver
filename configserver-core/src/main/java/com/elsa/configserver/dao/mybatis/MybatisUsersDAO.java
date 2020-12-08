package com.elsa.configserver.dao.mybatis;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

import com.elsa.configserver.dao.UsersDAO;
import com.elsa.configserver.domain.UsersDO;
import com.elsa.configserver.exception.DAOException;

@Component(value = "usersDAO")
public class MybatisUsersDAO extends MybatisBaseDAO implements UsersDAO {
	public Long insert(UsersDO usersDO) throws DAOException {
		int i = getSqlSession().insert("com.elsa.configserver.domain.UsersMapper.MybatisUsersDAO_insert", usersDO);
		if (i > 0) {
			return Long.valueOf(usersDO.getId());
		}
		return 0L;
	}

	public Integer update(UsersDO usersDO) throws DAOException {
		return getSqlSession().update("com.elsa.configserver.domain.UsersMapper.MybatisUsersDAO_updateById", usersDO);
	}

	public Integer deleteById(Long id) throws DAOException {
		return getSqlSession().delete("com.elsa.configserver.domain.UsersMapper.MybatisUsersDAO_deleteById", id);
	}

	public Integer updateDynamic(UsersDO usersDO) throws DAOException {
		return getSqlSession().update("com.elsa.configserver.domain.UsersMapper.MybatisUsersDAO_update_dynamic", usersDO);
	}

	public UsersDO selectById(Long id) throws DAOException {
		return getSqlSession().selectOne("com.elsa.configserver.domain.UsersMapper.MybatisUsersDAO_selectById", id);
	}

	public Long selectCountDynamic(UsersDO usersDO) throws DAOException {
		return getSqlSession().selectOne("com.elsa.configserver.domain.UsersMapper.MybatisUsersDAO_select_dynamic_count", usersDO);
	}

	public List<UsersDO> selectDynamic(UsersDO usersDO) throws DAOException {
		return getSqlSession().selectList("com.elsa.configserver.domain.UsersMapper.MybatisUsersDAO_select_dynamic", usersDO);
	}

	public List<UsersDO> selectDynamicPageQuery(UsersDO usersDO) throws DAOException {
		return getSqlSession().selectList("com.elsa.configserver.domain.UsersMapper.MybatisUsersDAO_select_dynamic_page_query", usersDO);
	}

}
