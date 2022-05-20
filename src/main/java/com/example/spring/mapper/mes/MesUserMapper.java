package com.example.spring.mapper.mes;

import com.example.spring.dto.FormDto;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface MesUserMapper {
    void resetPassword(String employeeNo);
}
