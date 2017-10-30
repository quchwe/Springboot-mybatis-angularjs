package com.quchwe.mapper;

import com.quchwe.Person;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PersonMapper {
    List<Person> findAllList();
}
