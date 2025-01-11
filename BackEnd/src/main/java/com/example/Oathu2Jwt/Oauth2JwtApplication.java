package com.example.Oathu2Jwt;

import com.example.Oathu2Jwt.Config.RSAKeyConfig.RSAKeyRecord;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
@SpringBootApplication
@EnableConfigurationProperties(RSAKeyRecord.class)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableCaching
@EnableScheduling
public class Oauth2JwtApplication {

	public static void main(String[] args) {
		try {
			// Create a MinioClient object
			MinioClient minioClient = MinioClient.builder()
					.endpoint("http://localhost:9000")
					.credentials("minioadmin", "minioadmin")
					.build();

			// Check if the bucket exists using BucketExistsArgs
			String bucketName = "mybucket";
			boolean isExist = minioClient.bucketExists(
					BucketExistsArgs.builder().bucket(bucketName).build()
			);
			if (isExist) {
				System.out.println("Bucket already exists.");
			} else {
				// Make a new bucket using MakeBucketArgs
				minioClient.makeBucket(
						MakeBucketArgs.builder().bucket(bucketName).build()
				);
				System.out.println("Bucket created successfully.");
			}

		} catch (MinioException e) {
			System.out.println("Error occurred: " + e);
		} catch (Exception e) {
			e.printStackTrace();
		}

		SpringApplication.run(Oauth2JwtApplication.class, args);
	}

}
