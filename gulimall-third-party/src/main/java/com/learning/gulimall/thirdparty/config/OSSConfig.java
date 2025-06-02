package com.learning.gulimall.thirdparty.config;

import com.alibaba.cloud.nacos.annotation.NacosConfig;
import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.SignVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OSSConfig {

//    @NacosConfig(dataId = "oss.property",group = "dev-group", key = "OSS_ACCESS_KEY_ID")
//    private String accessKeyId;
//
//    @NacosConfig(dataId = "oss.property",group = "dev-group", key = "OSS_ACCESS_KEY_SECRET")
//    private String accessKeySecret;
//
//    @NacosConfig(dataId = "oss.property",group = "dev-group", key = "OSS_ENDPOINT")
//    private String endpoint;
//
//    @NacosConfig(dataId = "oss.property",group = "dev-group", key = "OSS_REGION")
//    private String region;
//
//    @Bean
//    public OSS initOSSClient() {
//        // 创建DefaultCredentialProvider实例。
//        DefaultCredentialProvider defaultCredentialProvider = CredentialsProviderFactory.newDefaultCredentialProvider(accessKeyId, accessKeySecret);
//
//        // 创建OSSClient实例。
//        // 当OSSClient实例不再使用时，调用shutdown方法以释放资源。
//        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
//        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
//        OSS ossClient = OSSClientBuilder.create()
//                .endpoint(endpoint)
//                .credentialsProvider(defaultCredentialProvider)
//                .clientConfiguration(clientBuilderConfiguration)
//                .region(region)
//                .build();
//
//        return ossClient;
//    }
}