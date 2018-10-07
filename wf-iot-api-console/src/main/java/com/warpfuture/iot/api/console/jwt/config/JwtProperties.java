package com.warpfuture.iot.api.console.jwt.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author scolia
 */
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
@Component
public class JwtProperties {

    private Token token = new Token();

    private Keystore keystore = new Keystore();

    @Getter
    @Setter
    public static class Token {

        /**
         * header名称
         */
        private String header = "token";

        /**
         * 发行者
         */
        private String issuer = "wf";

        /**
         * 过期时间
         */
        private int expiry = 14 * 24 * 60 * 60 * 1000;
    }

    @Getter
    @Setter
    @SuppressWarnings("all")
    public static class Keystore {

        private boolean autoCreate = false;

        /**
         * KeyTool中已生成的KeyStore文件的位置
         */
        private String keystore = "jwt.jks";

        /**
         * KeyTool中生成KeyStore时设置的alias
         */
        private String alias;

        /**
         * KeyTool中生成KeyStore时设置的storepass
         */
        private String storepass;

        /**
         * 生成签名的算法
         */
        private String keyalg = "RSA";

        /**
         * 私钥的密码
         */
        private String keypass;

        /**
         * 指定证书拥有者信息
         */
        private Dname dname = new Dname();

        @Getter
        @Setter
        public static class Dname {

            /**
             * 名字和姓氏
             */
            private String CN = "wf";

            /**
             * 组织单位名称
             */
            private String OU = "warpfuture";

            /**
             * 组织名称
             */
            private String O = "wf";

            /**
             * 城市或区域名称
             */
            private String L = "guangzhou";

            /**
             * 州或省份名称
             */
            private String ST = "guangdong";

            /**
             * 单位的两字母国家代码
             */
            private String C = "CN";
        }

    }


}
