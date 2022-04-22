//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.spring.service;

import java.util.HashMap;

import com.example.spring.mapper.erp.ErpUserMapper;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CertificationService {

    private final ErpUserMapper erpUserMapper;

    public void certifiedPhoneNumber(String phoneNumber, String cerNum) {

        String api_key = "NCS84QRJLPA4PV04";
        String api_secret = "W4RCQQ1QJJUDKID5AGI4TRGFI5ZINEAX";
        Message coolsms = new Message(api_key, api_secret);
        HashMap<String, String> params = new HashMap();
        params.put("to", phoneNumber);
        params.put("from", "01045884951");
        params.put("type", "SMS");
        params.put("text", "휴대폰인증 메시지 : 인증번호는[" + cerNum + "]입니다.");
        params.put("app_version", "test app 1.2");

        try {
            JSONObject obj = coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException var8) {
            System.out.println(var8.getMessage());
            System.out.println(var8.getCode());
        }

    }
}
