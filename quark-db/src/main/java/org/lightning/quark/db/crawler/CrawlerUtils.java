package org.lightning.quark.db.crawler;

import schemacrawler.schemacrawler.SchemaInfoLevel;

/**
 * Created by cook on 2018/2/25
 */
public abstract class CrawlerUtils {

    /**
     * for date-fetcher
     * @return
     */
    public static SchemaInfoLevel createLevel4Data() {
        SchemaInfoLevel infoLevel = new SchemaInfoLevel();

        infoLevel.setRetrieveDatabaseInfo(true);

        infoLevel.setRetrieveColumnDataTypes(true);
        infoLevel.setRetrieveTableColumns(true);

        infoLevel.setRetrieveTables(true);
        infoLevel.setRetrieveTableConstraintInformation(true);
        infoLevel.setRetrieveTableConstraintDefinitions(true);
        infoLevel.setRetrieveAdditionalTableAttributes(true);

        return infoLevel;
    }

    /**
     * for schema-generate
     * @return
     */
    public static SchemaInfoLevel createLevel4generate() {
        SchemaInfoLevel infoLevel = new SchemaInfoLevel();

        infoLevel.setRetrieveDatabaseInfo(true);

        infoLevel.setRetrieveAdditionalJdbcDriverInfo(true);

        infoLevel.setRetrieveAdditionalColumnAttributes(true);
        infoLevel.setRetrieveColumnDataTypes(true);
        infoLevel.setRetrieveTableColumns(true);

        infoLevel.setRetrieveTables(true);
        infoLevel.setRetrieveTableConstraintInformation(true);
        infoLevel.setRetrieveTableConstraintDefinitions(true);
        infoLevel.setRetrieveAdditionalTableAttributes(true);

        infoLevel.setRetrieveIndexes(true);
        infoLevel.setRetrieveIndexInformation(true);
        infoLevel.setRetrieveIndexColumnInformation(true);

        return infoLevel;
    }

}
