package org.lightning.quark.core.feature;

import org.lightning.quark.core.model.message.MessageFeature;

/**
 * 行处理消息特征
 * Created by cook on 2018/3/21
 */
public abstract class RowFeature {

    /**
     * 以动作/版本构造
     */
    private static MessageFeature build(int action, int version) {
        return MessageFeature.builder()
                .messageType("QRK")
                .subType("row")
                .action(action)
                .version(version)
                .build();
    }

    public interface Action {

        /** 批量拷贝 */
        int STEP_ROW_COPY =  11000;

    }

    /** action 从  - 11000 开始 */
    public static class V1 {
        private static final int version = 1;

        /** 批量拷贝 */
        public final static MessageFeature STEP_ROW_COPY =  build(Action.STEP_ROW_COPY, version);


    }


}
