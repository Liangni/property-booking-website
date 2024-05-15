package com.penny.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

@Service
public class JsonConverter {
    private final JavaTimeModule javaTimeModule = new JavaTimeModule();

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(javaTimeModule);

    /**
     * 將物件轉換為 JSON 字串。
     *
     * @param object 要轉換為 JSON 的物件
     * @return 轉換後的 JSON 字串，如果輸入物件為空則返回 null
     */
    public String convertToJson(Object object) {
        if (object == null) return null;

        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON: " + e.getMessage(), e);
        }
    }

    /**
     * 將 JSON 字串轉換為指定類型的物件。
     *
     * @param json        要解析的 JSON 字串
     * @param valueTypeRef 用於描述目標類型的 TypeReference
     * @param <T>         目標類型
     * @return 解析後的目標物件，如果輸入 JSON 為空或"null"則返回 null
     * @throws IllegalArgumentException 如果解析過程中發生錯誤
     */
    public <T> T convertJsonToObject(String json, TypeReference<T> valueTypeRef)  {
        // 驗證 JSON 字串
        if (json == null || json.isEmpty() || json.equals("null")) {
            return null;
        }

        try {
            // 反序列化 JSON 成特定的物件
            return objectMapper.readValue(json, valueTypeRef);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing JSON: " + e.getMessage(), e);
        }
    }

    /**
     * 將 JSON 字串反序列化為指定類型的物件。
     *
     * @param json      要反序列化的 JSON 字串
     * @param valueType 目標類型的 Class 對象，用於指定反序列化後的物件類型
     * @param <T>       反序列化後的物件類型
     * @return 反序列化後的指定類型物件，如果 JSON 字串為空或為 "null"，則返回 null
     * @throws IllegalArgumentException 如果反序列化過程中發生錯誤，將拋出 IllegalArgumentException
     */
    public <T> T convertJsonToObject(String json, Class<T> valueType)  {
        // 驗證 JSON 字串
        if (json == null || json.isEmpty() || json.equals("null")) {
            return null;
        }

        try {
            // 反序列化 JSON 成特定的物件
            return objectMapper.readValue(json, valueType);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing JSON: " + e.getMessage(), e);
        }
    }
}
