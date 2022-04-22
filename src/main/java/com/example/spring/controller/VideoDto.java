package com.example.spring.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.File;

@Data
@RequiredArgsConstructor
public class VideoDto {

    private String fileName;
    private String path;

    public VideoDto(File file){
        this.fileName = file.getName();
        this.path = file.getPath();
    }

}
