package org.lightning.quark.core.model.db;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.lightning.quark.core.model.metadata.MetaPrimaryKey;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 主键 数据
 * Created by cook on 2018/2/25
 */
@Getter
@Setter
public class PKData {

    private MetaPrimaryKey primaryKey;
    private List<Pair<String, Object>> pkValues = new ArrayList<>(4);
    private Map<String, Object> pkValueMap = new HashMap<>(4);

    /**
     * 增加一个 主键 name/value
     * @param name
     * @param value
     */
    public void addOnePk(String name, Object value) {
        pkValues.add(Pair.of(name, value));
        pkValueMap.put(name, value);
    }

    /**
     * 数据主键 - 值
     * @return
     */
    public List<Object> getValues() {
        return pkValues.stream().map(Pair::getValue).collect(Collectors.toList());
    }

    public Object getOneValue(String pkName) {
        return pkValues.stream()
                .filter(pair -> StringUtils.equals(pair.getKey(), pkName))
                .map(Pair::getValue)
                .findFirst().orElse(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PKData other = (PKData) o;

        if (pkValues.size() != ((PKData) o).getPkValues().size()) return false;

        for (int i=0; i<pkValues.size(); i++) {
            if (!Objects.equals(pkValues.get(i).getValue(), other.getPkValues().get(i).getValue())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pkValues.toArray());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PK[(")
                .append(pkValues.stream()
                        .map(pair -> pair.getKey()+"="+pair.getValue())
                        .collect(Collectors.joining(","))
                )
                .append(")]");
        return sb.toString();
    }
}
