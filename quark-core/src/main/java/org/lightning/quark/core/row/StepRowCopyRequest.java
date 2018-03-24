package org.lightning.quark.core.row;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomUtils;
import org.lightning.quark.core.model.db.PKData;
import org.lightning.quark.core.model.message.ResourceIdentifier;

/**
 * 按步长拷贝数据
 * Created by cook on 2018/3/21
 */
@Getter
@Setter
public class StepRowCopyRequest implements ResourceIdentifier<Long> {

    private PKData startPk;
    private int pageSize;



    @Override
    public Long getResourceId() {
        return RandomUtils.nextLong();
    }

}
