package com.example.Oathu2Jwt;

import com.example.Oathu2Jwt.Config.RSAKeyConfig.RSAKeyRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableConfigurationProperties(RSAKeyRecord.class)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableCaching
public class Oauth2JwtApplication {

	public static void main(String[] args) {

			SpringApplication.run(Oauth2JwtApplication.class, args);
	}

}
