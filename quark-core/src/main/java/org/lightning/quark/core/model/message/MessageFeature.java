package org.lightning.quark.core.model.message;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.lightning.quark.core.utils.QuarkAssertor;

/**
 * Created by cook on 2018/3/19
 */
@Builder
@NoArgsConstructor
public class MessageFeature {

    /**
     * 消息类型 - 例如: 数据拷贝
     */
    @Getter
    @Setter
    private String messageType;

    /**
     * 子类型 - 例如: 批量拷贝
     */
    @Getter @Setter private String subType;

    /**
     * 动作 - 例如: 拷贝
     */
    @Getter @Setter private int action;

    /**
     * 版本 - 例如: 1, 2
     */
    @Getter @Setter private int version;

    private String featureText;

    public MessageFeature(String messageType, String subType, int action, int version, String featureText) {
        QuarkAssertor.isTrue(StringUtils.isNotEmpty(messageType), "消息类型不能为空");
        QuarkAssertor.isTrue(action > 0, "消息动作必须大于0");
        QuarkAssertor.isTrue(version > 0, "消息版本必须大于0");

        this.messageType = messageType;
        this.subType = subType;
        this.action = action;
        this.version = version;

        this.featureText = buildFeature();
    }

    /**
     * 构建消息特征文本
     */
    public String buildFeature() {
        if (featureText != null) {
            return featureText;
        }
        featureText = toFeatureText();
        return featureText;
    }

    private String toFeatureText() {
        StringBuilder sb = new StringBuilder();
        sb.append(messageType);
        if (StringUtils.isNotEmpty(subType)) {
            sb.append("ST").append(subType);
        }
        sb.append("AT").append(action).append("VR").append(version);
        return sb.toString();
    }

}
