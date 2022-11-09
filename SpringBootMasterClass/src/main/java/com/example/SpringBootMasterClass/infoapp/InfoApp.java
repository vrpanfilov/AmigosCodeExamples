package com.example.SpringBootMasterClass.infoapp;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("info.app")
@Data
public class InfoApp {
    private String name;
    private String description;
    private String version;
}
