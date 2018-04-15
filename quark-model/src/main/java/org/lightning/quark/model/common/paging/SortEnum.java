package org.lightning.quark.model.common.paging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.lightning.quark.core.enums.CodeFeature;

/**
 * Created by cook on 2018/4/14
 */
@Getter
@AllArgsConstructor
public enum SortEnum implements CodeFeature {

    ASC(1, "升序"),
    DESC(2, "降序")
    ;

    private int code;
    private String description;
}
