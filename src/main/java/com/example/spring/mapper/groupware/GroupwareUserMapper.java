//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.spring.mapper.groupware;
import com.example.spring.dto.EmployeeDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface GroupwareUserMapper {
    void resetPassword(String employeeNo);
}
