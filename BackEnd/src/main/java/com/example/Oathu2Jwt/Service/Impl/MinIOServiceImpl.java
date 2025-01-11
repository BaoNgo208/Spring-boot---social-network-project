package com.example.Oathu2Jwt.Service.Impl;

import com.example.Oathu2Jwt.Service.MinIOService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class MinIOServiceImpl implements MinIOService {
    private final MinioClient minioClient;


    @Override
    public String upLoadFile(MultipartFile file, String bucketName) {
        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            InputStream fileInputStream = file.getInputStream();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(fileInputStream, fileInputStream.available(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            fileInputStream.close();

            return String.format("http://localhost:9000/%s/%s", bucketName, fileName);

        } catch (MinioException minioException) {
            System.err.println(minioException.getMessage());
            throw new RuntimeException("Error occurred while uploading file to MinIO", minioException);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + file, e);
        } catch (IOException e) {
            throw new RuntimeException("I/O error occurred", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithm error occurred", e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Invalid key error occurred", e);
        }
    }

}
