package com.example.passwordinitializer.mapper.mes;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MesUserMapper {
    void resetPassword(String usr_id);
    void unlockAccount(String usr_id);
}
