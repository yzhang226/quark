package org.lightning.quark.core.model.db;

import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.lightning.quark.core.diff.DifferenceType;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by cook on 2018/3/1
 */
@Getter
@Setter
public class CopyResult {

    @Setter(AccessLevel.NONE)
    private Map<DifferenceType, Long> result = Maps.newHashMap();

    public void add(DifferenceType type, Number num) {
        result.put(type, result.getOrDefault(type, 0L) + (num == null ? 0 : num.longValue()));
    }

    public String toText() {
        return result.keySet().stream().map(type -> type + "=" + result.get(type)).collect(Collectors.joining(","));
    }

}
