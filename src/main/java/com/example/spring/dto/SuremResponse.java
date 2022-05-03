package com.example.spring.dto;

import lombok.Data;
import java.util.List;

@Data
public class SuremResponse {
    private String code;
    private String message;
    private List<Result> results;
}
