package org.lightning.quark.db.utils;

import org.lightning.quark.core.model.metadata.MetaColumn;
import org.lightning.quark.core.model.metadata.MetaPrimaryKey;
import org.lightning.quark.core.model.metadata.MetaTable;
import org.lightning.quark.db.crawler.CrawlerUtils;
import schemacrawler.schema.Catalog;
import schemacrawler.schema.Column;
import schemacrawler.schema.PrimaryKey;
import schemacrawler.schema.Table;

import java.sql.JDBCType;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by cook on 2018/2/25
 */
public abstract class MetadataConverter {

    public static List<MetaTable> convert(Collection<Table> tables) {
        return tables.stream()
                .map(table -> convert(table))
                .collect(Collectors.toList());
    }

    public static MetaTable convert(Table table) {
        if (table == null) {
            return null;
        }

        MetaTable metaTable = new MetaTable();
        metaTable.setName(table.getName());

//        table.getColumns().get(0).getOrdinalPosition()

        List<MetaColumn> metaColumns = table.getColumns().stream()
                .map(MetadataConverter::convert)
                .collect(Collectors.toList());
        metaTable.setColumns(metaColumns);
        metaTable.setPrimaryKey(convert(table.getPrimaryKey()));
        return metaTable;
    }

    public static MetaColumn convert(Column column) {
        if (column == null) {
            return null;
        }
        MetaColumn metaColumn = new MetaColumn();
        metaColumn.setName(column.getName());
        metaColumn.setType(column.getColumnDataType().getJavaSqlType().getJavaSqlType());
        metaColumn.setJdbcType(JDBCType.valueOf(column.getColumnDataType().getJavaSqlType().getJavaSqlType()));
        return metaColumn;
    }

    public static MetaPrimaryKey convert(PrimaryKey primaryKey) {
        if (primaryKey == null) {
            return null;
        }
        MetaPrimaryKey metaPrimaryKey = new MetaPrimaryKey();
        metaPrimaryKey.setName(primaryKey.getName());
        List<MetaColumn> metaColumns = primaryKey.getColumns().stream()
                .map(MetadataConverter::convert)
                .collect(Collectors.toList());
        metaPrimaryKey.setColumns(metaColumns);
        return metaPrimaryKey;
    }

}
