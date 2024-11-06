//package com.example.estsoft_udon_community.config;
//
//import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
//import org.springframework.beans.factory.annotation.Value;
//import org.jasypt.encryption.StringEncryptor;
//import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
//import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@EnableEncryptableProperties
//public class JasyptConfigAES {
//    @Value("${jasypt.encryptor.password}") // @Value어노테이션으로 properties 파일에 있는 값 읽어오도록 변경
//    private String password;
//
//    @Bean("jasyptEncryptorAES")
//    public StringEncryptor stringEncryptor() {
//        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
//        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
//
//        config.setPassword(password); // 지정한 암호화 키
//        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
//        config.setKeyObtentionIterations("1000");
//        config.setPoolSize("1");
//        config.setProviderName("SunJCE");
//        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
//        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
//        config.setStringOutputType("base64");
//        encryptor.setConfig(config);
//        return encryptor;
//    }
//}
