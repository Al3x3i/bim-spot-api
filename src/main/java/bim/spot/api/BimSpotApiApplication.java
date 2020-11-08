package bim.spot.api;

import bim.spot.api.icu.IcuApiProperties;
import bim.spot.api.icu.IcuRestTemplateInterceptor;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableConfigurationProperties(IcuApiProperties.class)
@EnableAsync
public class BimSpotApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BimSpotApiApplication.class, args);
    }


    @Autowired
    private IcuApiProperties icuApiProperties;

    @Bean
    public RestTemplate restTemplate(IcuApiProperties icuApiProperties) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(new IcuRestTemplateInterceptor(icuApiProperties)));
        return restTemplate;
    }

    @Bean("icuThreadPoolTaskExecutor")
    public TaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(100);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(500);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("Async-");
        return executor;
    }
}
