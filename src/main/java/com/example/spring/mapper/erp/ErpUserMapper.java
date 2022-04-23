//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.spring.mapper.erp;
import com.example.spring.dto.EmployeeDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface ErpUserMapper {
    void resetPassword(String employeeNo);
    EmployeeDto findEmployee(String employeeNo);
    String findAuthenticationCode(String employeeNo);
    void issueAuthenticationCode(Map<String, String> map);
    void disposeAuthenticationCode(String employeeNo);
}
