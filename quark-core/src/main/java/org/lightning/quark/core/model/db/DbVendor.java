package org.lightning.quark.core.model.db;

import org.apache.commons.lang3.StringUtils;

/**
 *    JDBC Driver Name                          - Product Name <br>
 * 1. Microsoft JDBC Driver 6.2 for SQL Server  - Microsoft SQL Server <br>
 * 2. MySQL Connector Java                      - MySQL <br>
 * 3. H2 JDBC Driver                            - H2 <br>
 * Created by cook on 2017/12/12
 */
public enum DbVendor {

    MYSQL, MSSQL, H2;

    public static DbVendor fromDriverName(String driverName) {
        if (StringUtils.containsIgnoreCase(driverName, "SQL Server")) {
            return MSSQL;
        } else if (StringUtils.containsIgnoreCase(driverName, "MySQL")) {
            return MYSQL;
        } else if (StringUtils.containsIgnoreCase(driverName, "H2 ")) {
            return H2;
        }
        return null;
    }

    public static DbVendor fromProductName(String productName) {
        if (StringUtils.containsIgnoreCase(productName, "Microsoft SQL Server")) {
            return MSSQL;
        } else if (StringUtils.containsIgnoreCase(productName, "MySQL")) {
            return MYSQL;
        } else if (StringUtils.containsIgnoreCase(productName, "H2")) {
            return H2;
        }
        return null;
    }

}
