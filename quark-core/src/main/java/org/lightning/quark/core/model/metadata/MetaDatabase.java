package org.lightning.quark.core.model.metadata;

import lombok.Getter;
import lombok.Setter;
import org.lightning.quark.core.model.db.DbVendor;

/**
 * Created by cook on 2018/2/25
 */
@Getter
@Setter
public class MetaDatabase {

    /**
     * vendor type of db
     */
    private DbVendor vendor;

    private String productName;

    private String productVersion;

    private String userName;

}
