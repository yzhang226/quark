package org.lightning.quark.core.model.json;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 */
public class JsonMap extends LinkedHashMap<Object, Object> {

    public JsonMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public JsonMap(int initialCapacity) {
        super(initialCapacity);
    }

    public JsonMap() {
    }

    public JsonMap(Map<?, ?> m) {
        super(m);
    }

    public JsonMap(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }

    public static JsonMap from(Map map) {
        return new JsonMap(map);
    }

}
