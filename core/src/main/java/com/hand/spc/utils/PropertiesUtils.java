package com.hand.spc.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public  class PropertiesUtils {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);
    private static final Properties properties = new Properties();
    private static final String DEFAULT_CONFIG_FILE = "messages/client.properties";
    static {
        try (InputStream in = PropertiesUtils.class.getClassLoader().getResourceAsStream(DEFAULT_CONFIG_FILE)) {
            properties.load(in);
        } catch (IOException e) {
            logger.error("{}>>{}>>{}", "PropertiesUtils", "getConfig", e.getMessage());
        }
    }
    public static Properties getConfig() {
        return properties;
    }
}
