package org.lightning.quark.core.model.db;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;
import org.lightning.quark.core.model.metadata.MetaPrimaryKey;

import java.util.ArrayList;
import java.util.List;

/**
 * 主键 数据
 * Created by cook on 2018/2/25
 */
@Getter
@Setter
public class PKData {

    private MetaPrimaryKey primaryKey;
    private List<Pair<String, Object>> pkValues = new ArrayList<>(4);

    /**
     * 增加一个 主键 name/value
     * @param name
     * @param value
     */
    public void addOnePk(String name, Object value) {
        pkValues.add(Pair.of(name, value));
    }

}
