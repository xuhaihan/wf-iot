package com.warpfuture.iot.api.console.jwt.config;


import com.warpfuture.iot.api.console.jwt.core.JwtKeyPairFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * jwt 初始化
 *
 * @author scolia
 */
@Component
public class JwtInit {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private JwtKeyPairFactory jwtKeyPairFactory;

    @EventListener
    public void init(ApplicationPreparedEvent event) throws Exception {
        Resource resource = new ClassPathResource(jwtProperties.getKeystore().getKeystore());
        if (!resource.exists()) {
            if (jwtProperties.getKeystore().isAutoCreate()) {
                this.createKeyStore();
                resource = new ClassPathResource(jwtProperties.getKeystore().getKeystore());
            } else {
                throw new FileNotFoundException();
            }
        }
        jwtKeyPairFactory.init(resource);
    }

    /**
     * 使用keytool创建keyStore
     *
     * @throws IOException
     */
    @SuppressWarnings("all")
    private void createKeyStore() throws Exception {
        Runtime run = Runtime.getRuntime();
        StringBuilder command = new StringBuilder();
        JwtProperties.Keystore keystore = jwtProperties.getKeystore();
        JwtProperties.Keystore.Dname dname = keystore.getDname();
        command.append("keytool")
                .append(" ").append("-genkey")
                .append(" ").append("-alias").append(" ").append(keystore.getAlias())
                .append(" ").append("-keyalg").append(" ").append(keystore.getKeyalg())
                .append(" ").append("-keypass").append(" ").append(keystore.getKeypass())
                .append(" ").append("-storepass").append(" ").append(keystore.getStorepass())
                .append(" ").append("-keystore").append(" ").append(keystore.getKeystore())
                .append(" ").append("-dname").append(" ")
                .append("CN=").append(dname.getCN()).append(",")
                .append("OU=").append(dname.getOU()).append(",")
                .append("O=").append(dname.getO()).append(",")
                .append("L=").append(dname.getL()).append(",")
                .append("ST=").append(dname.getST()).append(",")
                .append("C=").append(dname.getC());
        // 目前只能生成到编译后的目录中去, 在idea环境中, 可以在target/classes下找到, 必须将其拷贝到resource的根目录中去.
        File dir = new File(ClassUtils.getDefaultClassLoader().getResource("").getPath());
        Process process = run.exec(command.toString(), null, dir);
        process.waitFor();
    }
}
