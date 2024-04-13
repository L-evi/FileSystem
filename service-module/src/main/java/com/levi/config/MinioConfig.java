package com.levi.config;

import io.minio.MinioClient;
import io.minio.messages.Bucket;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "minio", ignoreUnknownFields = true)
@Slf4j
public class MinioConfig {


    @Setter
    private String url;

    @Setter
    private String accessKey;

    @Setter
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        MinioClient client = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
        try {
            List<Bucket> buckets = client.listBuckets();
            log.info("bucket list {}", buckets.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return client;
    }


}
