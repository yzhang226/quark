package org.lightning.quark.db.crawler;

import org.lightning.quark.core.model.db.DbVendor;
import org.lightning.quark.core.model.metadata.MetaCatalog;
import org.lightning.quark.core.model.metadata.MetaDatabase;
import schemacrawler.schema.Catalog;
import schemacrawler.schema.DatabaseInfo;
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

    public static MetaCatalog createCatalogInfo(Catalog catalog) {
        MetaCatalog cata = new MetaCatalog();
        cata.setDatabase(createDbInfo(catalog));
        return cata;
    }

    public static MetaDatabase createDbInfo(Catalog catalog) {
        MetaDatabase database = new MetaDatabase();
        DatabaseInfo info = catalog.getDatabaseInfo();
        database.setProductName(info.getProductName());
        database.setProductVersion(info.getProductVersion());
        database.setUserName(info.getUserName());
        database.setVendor(DbVendor.fromProductName(info.getProductName()));
        return database;
    }

}
