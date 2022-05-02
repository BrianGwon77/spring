//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.spring.controller;

import com.example.spring.Constant;
import com.example.spring.dto.AjaxReturnValue;
import com.example.spring.dto.AuthenticationDto;
import com.example.spring.dto.EmployeeDto;
import com.example.spring.service.CertificationService;
import com.example.spring.service.UserService;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    private final UserService userService;
    private final CertificationService certificationService;

    @GetMapping({"/sendSMS"})
    @ResponseBody
    public AjaxReturnValue sendSMS(HttpServletRequest request, String employeeNo) {

        EmployeeDto employeeDto = userService.findEmployee(employeeNo);
        String contact = employeeDto.getContact();

        /** 입력한 사번이 존재하지 않는 사번인 경우 혹은 휴대전화번호가 등록되어 있지 않은 경우 return false **/
        if (contact == null)
            return new AjaxReturnValue("false", "등록되지 않은 휴대전화번호입니다.");

        /** 입력 기간이 만료되지 않은 인증 번호가 존재할 경우 return false **/
        String authenticationCode = userService.findAuthenticationCode(employeeNo);
        if (authenticationCode != null)
            return new AjaxReturnValue("false", "잠시 후에 다시 시도해주세요");

        /** 관리자로 등록된 휴대전화번호인 경우 4자리 난수 생성 후 휴대폰 SMS 발송 **/
        Random rand = new Random();
        String numStr = "";

        for(int i = 0; i < 4; ++i) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr = numStr + ran;
        }

        System.out.println("수신자 번호 : " + contact);
        System.out.println("인증번호 : " + numStr);
        this.certificationService.certifiedPhoneNumber(contact, numStr);

        Map<String, String> map = new HashMap<String, String>();
        map.put("employeeNo" , employeeNo);
        map.put("authenticationCode", numStr);

        userService.issueAuthenticationCode(map);

        /** 4자리 난수 반환 **/
        return new AjaxReturnValue("success", numStr);

    }

    @GetMapping({"/resetPassword"})
    @ResponseBody
    public AjaxReturnValue resetPassword(HttpServletRequest request, int system) {
        String employeeNo = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.resetPassword(employeeNo, system);
        if (system == Constant.ERP || system == Constant.MES)
            return new AjaxReturnValue("true", "비밀번호 초기화가 완료되었습니다" + System.lineSeparator() + "아무 비밀번호나 입력하시면 초기화 화면이 나타납니다.");
        return new AjaxReturnValue("true", "비밀번호가 intops1234!로 초기화 되었습니다");

        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<String> req = new HttpEntity<>("request", httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange("aa", "post",req, )

    }

    @GetMapping({"/resetPasswordAdmin"})
    @ResponseBody
    public AjaxReturnValue resetPasswordAdmin(HttpServletRequest request, String employeeNo, int system) {
        userService.resetPassword(employeeNo, system);
        if (system == Constant.ERP || system == Constant.MES)
            return new AjaxReturnValue("true", "비밀번호 초기화가 완료되었습니다" + System.lineSeparator() + "아무 비밀번호나 입력하시면 초기화 화면이 나타납니다.");
        return new AjaxReturnValue("true", "그룹웨어 비밀번호가 intops1234!로 초기화 되었습니다");
    }

    @GetMapping({"/login"})
    public String certification(HttpServletRequest request, HttpServletResponse response, Model model) {
        Device device = DeviceUtils.getCurrentDevice(request);
        model.addAttribute("authenticationDto", new AuthenticationDto());
        if (device.isMobile())
            return "login_mobile";
        else
            return "login";
    }

    @GetMapping({"/main"})
    public String main(HttpServletRequest request, HttpServletResponse response) {
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
            return "main_admin";
        return "main";

    }

}
