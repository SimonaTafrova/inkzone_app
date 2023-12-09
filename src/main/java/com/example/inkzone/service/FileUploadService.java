package com.example.inkzone.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadService {

    String uploadFile(MultipartFile multipartFile) throws IOException;

    public void delete(String url) throws IOException;
}
