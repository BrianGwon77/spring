package com.example.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class SuremRequest {
    private String usercode;
    private String deptcode;
    private String yellowid_key;
    private List<Message> messages;

    public static SuremRequest createSuremRequest(List<Message> messages) {
        return new SuremRequest("intops", "F5-BQB-5L", "faa9159c01bbdcfb0f6ea9e941cae4d65ff78f4e", messages);
    }
}
