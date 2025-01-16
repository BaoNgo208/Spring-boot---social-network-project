package com.example.Oathu2Jwt.Controller;


import com.example.Oathu2Jwt.Service.MinIOService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class FileStorageController {
    private final MinIOService minIOService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadImage(@RequestParam(value = "file", required = false) MultipartFile file){
        return minIOService.upLoadFile(file, "mybucket");
    }



}
