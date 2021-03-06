//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.spring.controller;

import com.example.spring.Constant;
import com.example.spring.dto.*;
import com.example.spring.service.CertificationService;
import com.example.spring.service.RestTemplateService;
import com.example.spring.service.UserService;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.mysql.cj.util.Base64Decoder;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
import org.springframework.util.Base64Utils;
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
    private final RestTemplateService restTemplateService;

    @GetMapping({"/sendSMS"})
    @ResponseBody
    public AjaxReturnValue sendSMS(HttpServletRequest request, String employeeNo) {

        EmployeeDto employeeDto = userService.findEmployee(employeeNo);
        String contact = (employeeDto == null) ? null : employeeDto.getContact();

        /** ????????? ????????? ???????????? ?????? ????????? ?????? ?????? ????????????????????? ???????????? ?????? ?????? ?????? return false **/
        if (contact == null)
            return new AjaxReturnValue("false", "???????????? ?????? ???????????????.");

        /** ?????? ????????? ???????????? ?????? ?????? ????????? ????????? ?????? return false **/
        String authenticationCode = userService.findAuthenticationCode(employeeNo);
        if (authenticationCode != null)
            return new AjaxReturnValue("false", "?????? ?????? ?????? ??????????????????");

        /** ???????????? ????????? ????????????????????? ?????? 4?????? ?????? ?????? ??? ????????? SMS ?????? **/
        Random rand = new Random();
        String numStr = "";

        for(int i = 0; i < 4; ++i) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr = numStr + ran;
        }

        System.out.println("????????? ?????? : " + contact);
        System.out.println("???????????? : " + numStr);
        String code = this.restTemplateService.surem(contact, numStr);

        Map<String, String> map = new HashMap<String, String>();
        map.put("employeeNo" , employeeNo);
        map.put("authenticationCode", numStr);

        userService.issueAuthenticationCode(map);

        if (!code.equals("200"))
            return new AjaxReturnValue("false", "???????????? ????????? ?????????????????????.");

        /** 4?????? ?????? ?????? **/
        return new AjaxReturnValue("success", numStr);

    }

    @GetMapping("/axios")
    @ResponseBody
    public List<String> axios() {
        List<String> stringList = new ArrayList<String>();
        stringList.add("Jackson");
        stringList.add("Amber");
        stringList.add("Daphne");
        return stringList;
    }

    @GetMapping({"/resetPassword"})
    @ResponseBody
    public AjaxReturnValue resetPassword(HttpServletRequest request, int system) {
        String employeeNo = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.resetPassword(employeeNo, system);
        if (system == Constant.ERP || system == Constant.MES)
            return new AjaxReturnValue("true", "???????????? ???????????? ?????????????????????" + "<br>" + "?????? ??????????????? ??????????????? ????????? ????????? ???????????????.");
        return new AjaxReturnValue("true", "??????????????? intops1234!??? ????????? ???????????????");
    }

    @GetMapping({"/resetPasswordAdmin"})
    @ResponseBody
    public AjaxReturnValue resetPasswordAdmin(HttpServletRequest request, String employeeNo, int system) {
        userService.resetPassword(employeeNo, system);
        if (system == Constant.ERP || system == Constant.MES)
            return new AjaxReturnValue("true", "???????????? ???????????? ?????????????????????" + "<br>" + "?????? ??????????????? ??????????????? ????????? ????????? ???????????????.");
        return new AjaxReturnValue("true", "???????????? ??????????????? intops1234!??? ????????? ???????????????");
    }

    @GetMapping({"/login"})
    public String certification(HttpServletRequest request,
                                HttpServletResponse response,
                                @RequestParam(value="error", required = false) String error,
                                @RequestParam(value="exception", required = false) String exception,
                                Model model) {
        Device device = DeviceUtils.getCurrentDevice(request);
        model.addAttribute("authenticationDto", new AuthenticationDto());
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
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

    @PostMapping("/getFormList")
    @ResponseBody
    public Object getFormList() throws ParseException {

        /** ??????????????? ???????????? Load **/
        List<FormDto> formDtoList = userService.getFormList();
        List<FormAjaxDto> result = new ArrayList<FormAjaxDto>();

        for (FormDto formDto : formDtoList) {

            /** BodyContext Decoding **/
            String bodyContext = new String(Base64Utils.decodeFromString(formDto.getBodyContext()));

            /** Parse Json **/
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(bodyContext);

            /** Request Item **/
            String system = (jsonObject.get("Request_Item") == null) ? null : jsonObject.get("Request_Item").toString();

            if (system == null || !system.contains("Approval_Groupware"))
                continue;

            /** Application ID **/
            String id = (jsonObject.get("Application_ID") == null) ? " " : jsonObject.get("Application_ID").toString();

            if (!userService.isExistOnGroupware(id))
                result.add(FormAjaxDto.createFormAjaxDto(formDto.getSubject(), id, "Groupware",
                        formDto.getInitiatedDate(), formDto.getCompletedDate()));

        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", result);

        Object object = map;

        return object;

    }

    @GetMapping("/formList")
    public String formList(){
        log.info("formList called..");
        return "formList";
    }
}
