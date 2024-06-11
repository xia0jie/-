package com.reggie.controller;

import com.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private String BasePath;
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        String filename = file.getOriginalFilename();
        String fileSuffix = filename.substring(filename.lastIndexOf("."));
        String newfilename= UUID.randomUUID().toString() + fileSuffix;
        File dir = new File(BasePath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        try{
            file.transferTo(new File(BasePath+newfilename));
        }catch(IOException e){
            e.printStackTrace();
        }
        return R.success(newfilename);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        response.setContentType("image/jpeg");
        try {
            FileInputStream inputStream = new FileInputStream(BasePath+name);
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int len=0;
            while((len = inputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
