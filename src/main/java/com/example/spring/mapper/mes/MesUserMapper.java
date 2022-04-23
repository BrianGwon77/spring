package com.example.spring.mapper.mes;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MesUserMapper {
    void resetPassword(String employeeNo);
}
