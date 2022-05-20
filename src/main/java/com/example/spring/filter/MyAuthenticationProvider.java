package com.example.spring.filter;

import com.example.spring.dto.EmployeeDto;
import com.example.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MyAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String employeeNo = authentication.getName();
        String authenticationCode = userService.findAuthenticationCode(employeeNo);
        EmployeeDto employeeDto = userService.findEmployee(employeeNo);

        if (employeeDto == null)
            throw new BadCredentialsException("존재하지 않는 사번입니다.");

        if (employeeDto.getContact() == null)
            throw new BadCredentialsException("해당 사번과 매칭되는 휴대전화 정보가 없습니다.");

        if (authenticationCode == null)
            throw new BadCredentialsException("인증번호를 먼저 요청해주세요.");

        List<GrantedAuthority> roles = Arrays.stream(employeeDto.getAuthority().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        if (!(authentication.getCredentials().equals(authenticationCode)))
            throw new BadCredentialsException("인증번호를 다시 확인해주세요.");

        MyAuthenticationToken myAuthenticationToken = new MyAuthenticationToken(employeeNo, authenticationCode, roles);
        myAuthenticationToken.setAuthenticated(true);
        userService.disposeAuthenticationCode(employeeNo);
        return myAuthenticationToken;

    }

    @Override
    public boolean supports(Class<?> authentication) {
       // return authentication.isAssignableFrom(authentication);
        return true;
    }
}
