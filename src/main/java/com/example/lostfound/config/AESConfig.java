package com.example.lostfound.config;

import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Zero
 * @Date 2021/8/11 22:59
 * @Since 1.8
 * @Description
 **/
@Configuration
public class AESConfig {
    @Value("${aes.key}")
    String key;

    @Bean
    public SymmetricCrypto getSymmetricCrypto(){
        byte[] byteKey = key.getBytes();
        return new SymmetricCrypto(SymmetricAlgorithm.AES,byteKey);
    }
}
