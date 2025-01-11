package com.example.Oathu2Jwt.Service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public interface MinIOService {

    public String upLoadFile(MultipartFile file, String bucketName);
}
