package com.syu.capstone_stock.util;

import com.syu.capstone_stock.common.ApiResponse;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
    public static ResponseEntity<?> getResource(String Code, String filename){
        String filepath = System.getProperty("user.dir") + "/src/main/resources/static/graph/" + Code +"/" + filename;
        Resource resource = new FileSystemResource(filepath);
        if(!resource.exists())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        HttpHeaders header = new HttpHeaders();
        Path path;
        try{
            path = Paths.get(filepath);
            header.add("Content-Type", Files.probeContentType(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(resource, header, HttpStatus.OK);
    }
}
