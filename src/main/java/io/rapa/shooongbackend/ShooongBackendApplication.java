package io.rapa.shooongbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ShooongBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShooongBackendApplication.class, args);
    }

}
