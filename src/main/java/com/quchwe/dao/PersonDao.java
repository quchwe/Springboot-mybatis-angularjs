package com.quchwe.dao;

import com.quchwe.Person;
import org.apache.ibatis.session.SqlSession;

public class PersonDao {
    private final SqlSession sqlSession;

    public PersonDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public Person selectCityById(long id) {
        return this.sqlSession.selectOne("selectCityById", id);
    }
}
