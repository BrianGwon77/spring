package com.example.spring.dto;

import lombok.Data;

@Data
public class Message {
    /**
     * 수신자 휴대폰 번호
     **/
    private String callphone;
    /**
     * 메세지 내용
     **/
    private String text;
    /**
     * 템플릿 코드
     **/
    private String template_code;
}

