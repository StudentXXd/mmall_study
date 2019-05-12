package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public class PropertiesUtil {

//    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    private static Properties props;

    static {
        String fileName = "mmall.properties";
        props = new Properties();
        try {
            props.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName),"UTF-8"));
        } catch (IOException e) {
            log.error("配置文件读取异常",e);
        }
    }

    public static String getProperty(String key){
        String value = props.getProperty(key.trim());
        if (StringUtils.isBlank(value)){
            return null;
        }
        return value.trim();
    }

    public static String getProperty(String key,String defaultValue){
        String value = props.getProperty(key.trim());
        if (StringUtils.isBlank(value)){
            value = defaultValue;
        }
        return value.trim();
    }

    public static Integer getIntegerProperty(String key){
        Integer value =Integer.parseInt(props.getProperty(key.trim()));
        if ( Objects.isNull(value)){
            return 0;
        }
        return value;
    }

    public static Integer getIntegerProperty(String key,Integer defaultValue){
        Integer value =Integer.parseInt(props.getProperty(key.trim()));
        if (Objects.isNull(value)){
            value = defaultValue;
        }
        return value;
    }

    public static Boolean getBooleanProperty(String key){
        Boolean value =Boolean.parseBoolean(props.getProperty(key.trim()));
        if (value == null){
            return false;
        }
        return value;
    }

    public static Boolean getBooleanProperty(String key,Boolean defaultValue){
        Boolean value =Boolean.parseBoolean(props.getProperty(key.trim()));
        if (value == null){
            value = defaultValue;
        }
        return value;
    }
}
