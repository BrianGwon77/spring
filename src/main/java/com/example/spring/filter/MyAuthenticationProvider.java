package com.example.passwordinitializer.filter;

import com.example.passwordinitializer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MyAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String employeeNo = authentication.getName();
        String authenticationCode = userService.findAuthenticationCode(employeeNo);

        List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (authenticationCode == null) {
            throw new BadCredentialsException("please, send authentication code first.");
        }

        if (!(authentication.getCredentials().equals(authenticationCode)))
            throw new BadCredentialsException("please, check your authentication code again.");

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
