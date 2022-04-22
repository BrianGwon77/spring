package com.example.passwordinitializer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/video")
public class VideoController {

    private static final String DIR = "\\\\192.168.10.3\\교육영상\\";

    @PostMapping("/list")
    @ResponseBody
    public List<VideoDto> getVideoList(@RequestBody Map<String, String> map) {

        String system = map.get("system");

        File dir = new File(DIR + system);
        List<File> files = Arrays.asList(dir.listFiles());
        return files.stream().map(VideoDto::new).collect(Collectors.toList());

    }

    @GetMapping(path = "/play/{system}/{fileName}")
    public ResponseEntity<StreamingResponseBody> play(@PathVariable String system, @PathVariable String fileName) {

        File file = new File(DIR + system + "\\" + fileName);
        if (!file.isFile()) {
            return ResponseEntity.notFound().build();
        }

        StreamingResponseBody streamingResponseBody = new StreamingResponseBody() {
            @Override public void writeTo(OutputStream outputStream) throws IOException {
                try {
                    final InputStream inputStream = new FileInputStream(file);
                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = inputStream.read(bytes)) >= 0) {
                        outputStream.write(bytes, 0, length);
                    }
                    inputStream.close();
                    outputStream.flush();
                } catch (final Exception e) {

                }
            }
        };

        final HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "video/mp4");
        responseHeaders.add("Content-Length", Long.toString(file.length()));

        return ResponseEntity.ok().headers(responseHeaders).body(streamingResponseBody);

    }

    @GetMapping("/download/{system}/{fileName}")
    public String videoDownload(@PathVariable String system, @PathVariable String fileName, Model model) {
        model.addAttribute("videoUrl", "/video/download/play/" + system + "/" + fileName);
        return "video";
    }

    @GetMapping("/download/play/{system}/{fileName}")
    public void videoDownloadFileName(@PathVariable String system, @PathVariable String fileName, HttpServletRequest req, HttpServletResponse res) throws IOException {
        String fileFullPath = DIR + system + "\\" + fileName;
        File downFile = new File(fileFullPath);
        /** 파일 객체 생성 **/
        if(downFile.isFile()){
            /** 파일이 존재한다면.. **/
            int fSize = (int)downFile.length();
            res.setBufferSize(fSize);
            res.setContentType("application/octet-stream");
            res.setHeader("Content-Disposition", "attachment; filename=" + fileName + ";");
            res.setContentLength(fSize);
            /** 헤더정보 입력 **/
            FileInputStream in = new FileInputStream(downFile);
            ServletOutputStream out = res.getOutputStream();
            try {
                /** 8 Byte 단위 **/
                byte[] buf = new byte[8192];
                int bytesread = 0, bytesBuffered = 0;
                while( (bytesread = in.read( buf )) > -1 ) {
                    out.write( buf, 0, bytesread );
                    bytesBuffered += bytesread;
                    if (bytesBuffered > 1024 * 1024) {
                        /** OutputStream이 1MB가 넘어간다면 Flush **/
                        bytesBuffered = 0;
                        out.flush();
                    }
                }
            } finally {
                if (out != null) {
                    out.flush();
                    out.close();
                }
                if (in != null) {
                    in.close();
                } /** 에러가 나도 Output Flush 와 Close 실행 **/
            }
        }
    }

    @GetMapping("/resource/{system}/{fileName}")
    public String videoResource(@PathVariable String system, @PathVariable String fileName, Model model) {
        model.addAttribute("videoUrl", "/video/resource/play/" + system + "/" + fileName);
        return "video";
    }

    @GetMapping("/resource/play/{system}/{fileName}")
    public ResponseEntity<Resource> videoResourceFileName(@PathVariable String system, @PathVariable String fileName) throws FileNotFoundException {
        String fileFullPath = DIR + system + "\\" + fileName;
        Resource resource = new FileSystemResource(fileFullPath);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + "");
        headers.setContentType(MediaType.parseMediaType("video/mp4"));
        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
    }

}
