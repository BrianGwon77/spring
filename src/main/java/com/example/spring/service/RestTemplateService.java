package com.example.spring.service;

import com.example.spring.dto.Message;
import com.example.spring.dto.SuremRequest;
import com.example.spring.dto.SuremResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestTemplateService {

    public String surem(String contact, String certifiedNumber){

        URI uri = UriComponentsBuilder
                .fromUriString("https://rest.surem.com")
                .path("/biz/at/v2/json")
                .encode()
                .build()
                .toUri();

        System.out.println(uri.toString());

        List<Message> messages = new ArrayList<Message>();
        Message newMessage = new Message();
        newMessage.setTemplate_code("intops_007");
        newMessage.setText("[ 사용자 비밀번호 초기화 인증번호 안내 ] 본인확인 인증번호는 " + certifiedNumber + "입니다.");
        newMessage.setCallphone("82-" + contact.substring(1, contact.length()));
        messages.add(newMessage);

        SuremRequest suremRequest = SuremRequest.createSuremRequest(messages);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SuremResponse> suremResponse = restTemplate.postForEntity(uri, suremRequest, SuremResponse.class);

        log.info("surem Response : {}", suremResponse);

        return suremResponse.getBody().getCode();

    }

}
