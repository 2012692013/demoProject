package com.qyh.demo.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ContextConstants {

    public static String PROFILES_ACTIVE;

    public static String REDIS_HOST;

    public static Integer REDIS_PORT;

    public static String REDIS_PASSWORD;

    public static String SERVER_PATH;

    public static Boolean PARAM_VALID;

    @Value("${swagger.param-valid.enable}")
    public void setParamValid(Boolean paramValid) {
        PARAM_VALID = paramValid;
    }

    @Value("${server.servlet.context-path}")
    public void setServerPath(String serverPath) {
        SERVER_PATH = serverPath.equals("/")?"":serverPath;
    }

    @Value("${spring.profiles.active}")
    public void setProfilesActive(String profilesActive) {
        PROFILES_ACTIVE = profilesActive;
    }

    @Value("${spring.redis.host}")
    public void setRedisHost(String redisHost) {
        REDIS_HOST = redisHost;
    }

    @Value("${spring.redis.port}")
    public void setRedisPort(Integer redisPort) {
        REDIS_PORT = redisPort;
    }

    @Value("${spring.redis.password}")
    public void setRedisPassword(String redisPassword) {
        REDIS_PASSWORD = redisPassword;
    }

}
