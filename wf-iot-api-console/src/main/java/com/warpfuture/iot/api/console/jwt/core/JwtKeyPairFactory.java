package com.warpfuture.iot.api.console.jwt.core;

import com.warpfuture.iot.api.console.jwt.config.JwtProperties;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.RSAPublicKeySpec;

/**
 * @author scolia
 */
@Getter
@Component
public class JwtKeyPairFactory {

    private KeyPair keyPair;

    @Autowired
    private JwtProperties jwtProperties;

    public void init(Resource resource) throws Exception {
        // 加载Keystore文件
        KeyStore keyStore = KeyStore.getInstance("jks");
        keyStore.load(resource.getInputStream(), jwtProperties.getKeystore().getStorepass().toCharArray());
        // 获取keyPair
        RSAPrivateCrtKey privateKey = (RSAPrivateCrtKey) keyStore.getKey(jwtProperties.getKeystore().getAlias(), jwtProperties.getKeystore().getKeypass().toCharArray());
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(privateKey.getModulus(), privateKey.getPublicExponent());
        PublicKey publicKey = KeyFactory.getInstance(jwtProperties.getKeystore().getKeyalg()).generatePublic(publicKeySpec);
        this.keyPair = new KeyPair(publicKey, privateKey);
    }
}
