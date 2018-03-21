package org.lightning.quark.core.message;

import org.apache.commons.collections.CollectionUtils;
import org.lightning.quark.core.exception.QuarkExecuteException;
import org.lightning.quark.core.model.message.ActionMessage;
import org.lightning.quark.core.model.message.MessageFeature;
import org.lightning.quark.core.model.message.ResourceIdentifier;
import org.lightning.quark.core.utils.ClazzUtils;
import org.lightning.quark.core.utils.QuarkAssertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * 基础动作处理器, 确保是单例
 * Created by cook on 2017/7/17.
 */
public abstract class BaseActionProcessor<T extends ResourceIdentifier> implements ActionProcessor<T> {

    private static final Logger logger = LoggerFactory.getLogger(BaseActionProcessor.class);

    protected Class<T> resourceClass;

    public BaseActionProcessor() {
        resourceClass = ClazzUtils.getSuperClassGenericType(getClass(), 0);
        ActionProcessorFactory.registerProcessor(this);
    }

    public ProcessResult process(ActionMessage<T> message) {
        return process(message.getFeature(), message.getResource());
    }

    public ProcessResult process(MessageFeature feature, T resource) {
        if (logger.isDebugEnabled()) {
            logger.debug("process message feature#{}, resourceId#{}, data#{}",
                    Objects.nonNull(feature)? feature.buildFeature() : "NONE",
                    Objects.nonNull(resource) ? resource.getResourceId() : "NONE",
                    Objects.nonNull(resource) ? (resource) : "NONE");
        }

        try {
            ProcessResult result = processResource(resource);
            return result;
        } catch (Exception e) {
            throw new QuarkExecuteException("process resource#" + resource.getResourceId() + " error",  e);
        } finally {

        }

    }

    @Override
    public String getActorName() {
        QuarkAssertor.isTrue(CollectionUtils.isNotEmpty(getMessageFeatures()), "处理器对应的消息特征(s)不能为空");
        if (getMessageFeatures().size() == 1) {
            return getClass().getSimpleName() + "-" + getMessageFeatures().get(0).buildFeature();
        }

        throw new QuarkExecuteException("处理器对应的消息特征(s)数量大于1, 需自定义ActionName");
    }

    /**
     * 处理资源
     * @param resource
     * @return
     */
    protected abstract ProcessResult processResource(T resource);

    public Class<T> getResourceClass() {
        return resourceClass;
    }

    @Override
    public String getDispatcherName() {
        return null;
    }

}
