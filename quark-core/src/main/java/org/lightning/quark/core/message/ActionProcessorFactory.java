package org.lightning.quark.core.message;

import org.apache.commons.collections.CollectionUtils;
import org.lightning.quark.core.model.message.MessageFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 动作处理器工厂
 * Created by cook on 2017/7/17.
 */
public class ActionProcessorFactory {

    private static final Logger logger = LoggerFactory.getLogger(ActionProcessorFactory.class);

    private static final Map<String, Set<ActionProcessor>> processors = new ConcurrentHashMap<>();

    /**
     *
     * @param processor
     */
    public static void registerProcessor(ActionProcessor processor) {
        List<MessageFeature> features = processor.getMessageFeatures();
        if (CollectionUtils.isEmpty(features)) {
            logger.warn("processor#{} has none features", processor);
            return;
        }
        features.forEach(feature -> putProcessor(feature.buildFeature(), processor));
    }

    private static void putProcessor(String featureText, ActionProcessor processor) {
        processors.computeIfAbsent(featureText, k -> new HashSet<>()).add(processor);
        logger.info("add process#{} with feature#{}", processor, featureText);
    }

    /**
     * 消息特征 对应 处理器(s)
     * @param feature
     * @return
     */
    public static Set<ActionProcessor> getProcessors(MessageFeature feature) {
        return getProcessors(feature.buildFeature());
    }

    /**
     * 消息特征文本 对应 处理器(s)
     * @param featureText
     * @return
     */
    public static Set<ActionProcessor> getProcessors(String featureText) {
        return processors.getOrDefault(featureText, Collections.emptySet());
    }

    public static Set<ActionProcessor> getAllProcessors() {
        return processors.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

}
