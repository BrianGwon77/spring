package com.example.spring.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FormAjaxDto {

    private String subject;
    private String applicationID;
    private String system;
    private LocalDateTime initiatedDate;  /** 상신일 **/
    private LocalDateTime completedDate; /** 완료일 **/

    public static FormAjaxDto createFormAjaxDto(String subject
                                                ,String applicationID, String system
                                                ,LocalDateTime initiatedDate, LocalDateTime completedDate)
    {
        FormAjaxDto formAjaxDto = new FormAjaxDto();
        formAjaxDto.setSubject(subject);
        formAjaxDto.setApplicationID(applicationID);
        formAjaxDto.setSystem(system);
        formAjaxDto.setInitiatedDate(initiatedDate);
        formAjaxDto.setCompletedDate(completedDate);

        return formAjaxDto;
    }

}
