package org.lightning.quark.core.message;


import org.lightning.quark.core.model.message.ActionMessage;
import org.lightning.quark.core.model.message.MessageFeature;
import org.lightning.quark.core.model.message.ResourceIdentifier;

import java.util.List;

/**
 * 动作消息处理器
 * Created by cook on 2017/7/17.
 */
public interface ActionProcessor<T extends ResourceIdentifier> {

    /**
     *
     * @param message
     */
    ProcessResult process(ActionMessage<T> message);

    /**
     * 获取actorName
     * @return
     */
    String getActorName();

    /**
     * 资源类型
     * @return
     */
    Class<T> getResourceClass();

    /**
     * 分发器-名称, 根据不同 名称, 使用不同的分发 配置
     * @return
     */
    String getDispatcherName();

    /**
     * 每一个消息处理器, 对应多个处理对应的消息特征的消息
     * @return
     */
    List<MessageFeature> getMessageFeatures();

}
