package com.paintourcolor.odle.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey; // AWS S3 접근 accessKey

    @Value("${cloud.aws.credentials.secretKey}") // AWS S3 접근 secretKey
    private String secretKey;

    @Value("${cloud.aws.region.static}") // AWS S3 bucket이 위치한 region
    private String region;

    @Bean
    public AmazonS3 amazonS3Client() {
        // AWS 인증 자격 구성
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        // Amazon S3 클라이언트를 빌드
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(region)
                .build();
    }
}