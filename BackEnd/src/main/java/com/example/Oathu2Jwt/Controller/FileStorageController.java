package com.example.Oathu2Jwt.Controller;


import com.example.Oathu2Jwt.Service.MinIOService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FileStorageController {
    private final MinIOService minIOService;

//    @GetMapping("/upload")
//    public String uploadImage(@RequestParam String filePath){
//        String bucketName = "mybucket";
//        filePath = filePath.replace("\\", "/");
//        minIOService.upLoadFile(filePath, bucketName);
//        return "File uploaded successfully!";
//    }

//    @GetMapping("/getPostImage")
//    public String getImageUrl()

}
