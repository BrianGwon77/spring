//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.passwordinitializer.service;

import com.example.passwordinitializer.mapper.erp.ErpUserMapper;
import com.example.passwordinitializer.mapper.mes.MesUserMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional(
        readOnly = true
)
@RequiredArgsConstructor
public class UserService {

    /**
        1. Http Request > ServletFilter (Servlet Container)
        2. DelegatingFilterProxy > FilterChainProxy (Spring Container)
           FilterChainProxy - List<FilterChain> (Matchers, List<Filter>)
           사용자의 Request (요청 자원, URL)이 Matchers와 일치한다면 List<Filter> 의 Filter들을 순차적으로 통과시킨다.
    **/
    private enum SYSTEM { ERP, MES };
    private final ErpUserMapper erpUserMapper;
    private final MesUserMapper mesUserMapper;

    public void unlockAccount(String usr_id, int system) {
        if (SYSTEM.ERP.equals(system))
            this.erpUserMapper.unlockAccount(usr_id);
        else
            this.mesUserMapper.unlockAccount(usr_id);
    }

    public String findEmployee(String employeeNo){
        return erpUserMapper.findEmployee(employeeNo);
    }

    public void resetPassword(String usr_id, int system) {
        if (SYSTEM.ERP.equals(system))
            this.erpUserMapper.resetPassword(usr_id);
        else
            this.mesUserMapper.resetPassword(usr_id);
    }

    public String findAuthenticationCode(String employeeNo){
        return erpUserMapper.findAuthenticationCode(employeeNo);
    }

    public void issueAuthenticationCode(Map<String, String> map){
        erpUserMapper.issueAuthenticationCode(map);
    }

    public void disposeAuthenticationCode(String employeeNo){
        erpUserMapper.disposeAuthenticationCode(employeeNo);
    }

}
