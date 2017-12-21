package xyz.fz.netty.notify.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static xyz.fz.netty.notify.util.Constants.DELIMITER_STR;

public class BaseUtil {

    public static ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(8);

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T parseJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // done 防止误用，屏蔽为private
    private static String toJson(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String toDelimiterJson(Object o) {
        try {
            return objectMapper.writeValueAsString(o) + DELIMITER_STR;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
