//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.spring.mapper.groupware;
import com.example.spring.dto.EmployeeDto;
import com.example.spring.dto.FormDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GroupwareUserMapper {
    void resetPassword(String employeeNo);
    List<FormDto> getFormList();
    String isExist(String id);
}
