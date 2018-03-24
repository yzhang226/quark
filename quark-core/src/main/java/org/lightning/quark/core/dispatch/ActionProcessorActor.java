package org.lightning.quark.core.dispatch;

import akka.actor.AbstractActor;
import org.lightning.quark.core.message.ActionProcessor;
import org.lightning.quark.core.model.message.ActionMessage;
import org.lightning.quark.core.utils.QuarkAssertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息处理器actor
 *
 */
public class ActionProcessorActor extends AbstractActor {

    private static final Logger logger = LoggerFactory.getLogger(ActionProcessorActor.class);

    private ActionProcessor processor;

    public ActionProcessorActor(ActionProcessor processor) {
        QuarkAssertor.isTrue(processor != null, "processors must not be empty or null");
        this.processor = processor;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ActionMessage.class, msg -> {
                    logger.debug("actor#{} process msg#{}, data#{}", this,
                            msg.getFeature().buildFeature(), msg.getResource().getResourceId());
                    processor.process(msg);
                })
                .matchAny(obj -> {
                    logger.error("unknown message object#{}", obj);
                })
                .build();
    }


}
