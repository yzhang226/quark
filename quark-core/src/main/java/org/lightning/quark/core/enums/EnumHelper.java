package org.lightning.quark.core.enums;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 枚举帮助类
 */
public final class EnumHelper {

    public static final Map<Class<? extends CodeFeature>, Map<Integer, ? extends CodeFeature>> codeEnumHolders = Maps.newHashMap();

    /**
     * 注册枚举类 对应的 所有枚举值
     * @param enumClass
     */
    public static void register(Class<? extends CodeFeature> enumClass) {
        CodeFeature[] features = enumClass.getEnumConstants();
        if (ArrayUtils.isEmpty(features)) {
            return;
        }
        Map<Integer, CodeFeature> mm = Arrays.stream(features).collect(Collectors.toMap(CodeFeature::getCode, x -> x));
        codeEnumHolders.put(enumClass, mm);
    }

    /**
     * 从<code>code</code>, 获取枚举实例
     * @param code
     * @param enumClass
     * @param <T>
     * @return
     */
    public static <T extends CodeFeature> T valueOf(Integer code, Class<T> enumClass) {
        if (codeEnumHolders.containsKey(enumClass)) {
            return (T) codeEnumHolders.get(enumClass).get(code);
        }
        register(enumClass);
        return (T) codeEnumHolders.get(enumClass).get(code);
    }

    /**
     * 获取一个枚举类型的所有的枚举值
     * @param enumClass
     * @return
     */
    public static <T> List<T> getEnums(Class<T> enumClass) {
        return Arrays.asList(enumClass.getEnumConstants());
    }

    /**
     * 数字, 文本值(不区分大小写), 统统给你转换成 枚举
     * @param source
     * @param enumType
     * @param <T>
     * @return
     */
    public static <T extends Enum> T textToEnum(String source, Class<T> enumType) {
        if (StringUtils.isBlank(source)) {
            return null;
        }

        if (!enumType.isEnum()) {
            throw new RuntimeException("class[" + enumType + "] MUST be enum-class");
        }

        source = StringUtils.trimToEmpty(source);

        if (CodeFeature.class.isAssignableFrom(enumType)) {
            if (NumberUtils.isDigits(source)) {
                return (T) EnumHelper.valueOf(Integer.valueOf(source), (Class<? extends CodeFeature>) enumType);
            }
        }

        T[] enumValues = enumType.getEnumConstants();
        for (int i = enumValues.length; --i >= 0; ) {
            T e = enumValues[i];
            if (StringUtils.equalsIgnoreCase(e.toString(), source)) {
                return e;
            }
        }

        throw new RuntimeException("no suitable enum for text[" + source + "] in enum-class[" + enumType + "]");
    }

}
