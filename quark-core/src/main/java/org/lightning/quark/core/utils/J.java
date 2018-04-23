package org.lightning.quark.core.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.lightning.quark.core.exception.JsonParseRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cook on 2018/4/23
 */
public class J {

    private static final Logger logger = LoggerFactory.getLogger(J.class);

    private static final ObjectMapper mapper;

    static {
        mapper = createMapper();
    }


    /**
     * 创建ObjectMapper实例, <br>
     * 1. 忽略未知的属性, <br>
     * 2. 允许控制字符, <br>
     * 3. 注册JodaModule, 解析DateTime, <br>
     *
     * @return
     */
    public static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        mapper.configure(MapperFeature.ALLOW_EXPLICIT_PROPERTY_RENAMING, true);

        // 忽略大小写
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        // 兼容lombok
        mapper.configure(MapperFeature.INFER_CREATOR_FROM_CONSTRUCTOR_PROPERTIES, false);
        // 禁用自动检测public属性, 必须使用getter-setter
        mapper.configure(MapperFeature.AUTO_DETECT_FIELDS, false);

        JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);

        return mapper;
    }

    /**
     * 转换一个对象为json字符串
     * @param obj
     * @return
     */
    public String toJson(Object obj) {
        if (obj == null) {
            return "";
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("Write object to json error", e);
            throw new JsonParseRuntimeException("Write object to json error", e);
        }
    }

    /**
     * 转换一个json字符串为一个对象(指定class)
     * @param jsonText
     * @param clz
     * @param <T>
     * @return
     */
    public <T> T toObj(String jsonText, Class<T> clz) {
        if (StringUtils.isEmpty(jsonText)) {
            return null;
        }
        try {
            return mapper.readValue(jsonText, clz);
        } catch (IOException e) {
            logger.error("Parse Json to Object error", e);
            throw new JsonParseRuntimeException("Parse Json to Object error", e);
        }
    }

    /**
     * 转换 map 为一个对象(指定class)
     * @param map
     * @param clz
     * @param <T>
     * @return
     */
    public <T> T toObj(Map map, Class<T> clz) {
        if (MapUtils.isEmpty(map)) {
            return null;
        }
        try {
            return mapper.convertValue(map, clz);
        } catch (Exception e) {
            logger.error("Parse Json to Object error", e);
            throw new JsonParseRuntimeException("Parse Json to Object error", e);
        }
    }

    /**
     * 转换为Map结构
     * @param jsonText
     * @return
     */
    public Map toMap(String jsonText) {
        return toObj(jsonText, LinkedHashMap.class);
    }

    /**
     * 转换为List结构
     * @param jsonText
     * @return
     */
    public List toList(String jsonText) {
        return toObj(jsonText, ArrayList.class);
    }

    /**
     * 转换为数组结构
     * @param jsonText
     * @return
     */
    public Object[] toArray(String jsonText) {
        return toObj(jsonText, Object[].class);
    }

    /**
     * 转换为Map结构, 且指定key的类型和value的类型
     * @param jsonText json字符串
     * @param keyClass key的class类型
     * @param valueClass value的class类型
     * @param <K>
     * @param <V>
     * @return
     */
    public <K, V> Map<K, V> toMap(String jsonText, Class<K> keyClass, Class<V> valueClass) {
        if (StringUtils.isEmpty(jsonText)) {
            return null;
        }
        JavaType type = mapper.getTypeFactory().constructMapLikeType(LinkedHashMap.class, keyClass, valueClass);
        return readByJavaType(jsonText, type);
    }

    /**
     * 转换为数组结构, 指定数组类的元素类型
     * @param jsonText
     * @param elementClass 数组内元素类型
     * @param <T>
     * @return
     */
    public <T> T[] toArray(String jsonText, Class<T> elementClass) {
        if (StringUtils.isEmpty(jsonText)) {
            return null;
        }
        JavaType type = mapper.getTypeFactory().constructArrayType(elementClass);
        return readByJavaType(jsonText, type);
    }

    /**
     * 转换为List结构, 指定List内的元素类型
     * @param jsonText
     * @param elementClass 集合内元素类型
     * @param <T>
     * @return
     */
    public <T> List<T> toList(String jsonText, Class<T> elementClass) {
        if (StringUtils.isEmpty(jsonText)) {
            return null;
        }
        JavaType type = mapper.getTypeFactory().constructCollectionLikeType(ArrayList.class, elementClass);
        return readByJavaType(jsonText, type);
    }

    /**
     * 使用指定的TypeReference来转换json字符串
     * @param jsonText
     * @param refer 类型引用
     * @param <T>
     * @return
     */
    public <T> T toObjByReference(String jsonText, TypeReference<T> refer) {
        if (StringUtils.isEmpty(jsonText)) {
            return null;
        }
        try {
            return mapper.readValue(jsonText, refer);
        } catch (IOException e) {
            logger.error("Parse Json to Object by reference error", e);
            throw new JsonParseRuntimeException("Parse Json to Object by reference error", e);
        }
    }

    /**
     * 转换json字符串, 指定实体类和泛型类
     * @param jsonText json字符串
     * @param objClass 实体类
     * @param genericClasses 泛型类
     * @param <T>
     * @return
     */
    public <T> T toObjByGenericType(String jsonText, Class<T> objClass, Class<?>... genericClasses) {
        if (StringUtils.isEmpty(jsonText)) {
            return null;
        }
        if (String.class.isAssignableFrom(objClass)) {
            return (T) jsonText;
        }

        JavaType type = mapper.getTypeFactory().constructParametricType(objClass, genericClasses);
        return readByJavaType(jsonText, type);
    }

    public <T> T readByJavaType(String jsonText, JavaType javaType) {
        if (StringUtils.isEmpty(jsonText)) {
            return null;
        }
        try {
            return mapper.readValue(jsonText, javaType);
        } catch (Exception e) {
            logger.error("Parse Json by JavaType#{} error", javaType, e);
            throw new JsonParseRuntimeException("Parse Json by JavaType#" + javaType + " error", e);
        }
    }


}
