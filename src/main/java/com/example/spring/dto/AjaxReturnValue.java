package com.example.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AjaxReturnValue {
    String result;
    String msg;
}
