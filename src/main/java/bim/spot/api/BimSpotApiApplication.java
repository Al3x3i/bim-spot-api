package bim.spot.api;

import bim.spot.api.icu.IcuApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableConfigurationProperties(IcuApiProperties.class)
@EnableAsync
public class BimSpotApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BimSpotApiApplication.class, args);
    }

}
