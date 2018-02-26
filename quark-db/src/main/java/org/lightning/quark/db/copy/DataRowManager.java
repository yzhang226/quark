package org.lightning.quark.db.copy;

import org.lightning.quark.core.model.metadata.MetaTable;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * Created by cook on 2018/2/26
 */
public class DataRowManager {

    private MetaTable table;

    private DataSource dataSource;

    public DataRowManager(MetaTable table, DataSource dataSource) {
        this.table = table;
        this.dataSource = dataSource;
    }

    private List<Map<String, Object>> fetchRows() {

        return null;
    }

    private List<Map<String, Object>> insertRows(List<Map<String, Object>> rows) {

        return null;
    }

}
