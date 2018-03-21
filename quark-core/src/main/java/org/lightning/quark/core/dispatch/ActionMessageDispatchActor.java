package org.lightning.quark.core.dispatch;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import org.apache.commons.lang3.ObjectUtils;
import org.lightning.quark.core.akka.ActorBuilder;
import org.lightning.quark.core.message.ActionProcessor;
import org.lightning.quark.core.message.ActionProcessorFactory;
import org.lightning.quark.core.model.message.ActionMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cook on 2018/3/19
 */
public class ActionMessageDispatchActor extends AbstractActor {

    private static final Logger logger = LoggerFactory.getLogger(ActionMessageDispatchActor.class);

    private boolean isShardEnable;

    private int mode;

    public ActionMessageDispatchActor(boolean isShardEnable, int mode) {
        this.isShardEnable = isShardEnable;
        this.mode = mode;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ActionMessage.class, message -> {
                    logger.info("message is {}", message);
                    process(message);
                })
                .build();
    }

    private void process(ActionMessage message) {
        ActionProcessorFactory.getProcessors(message.getFeature().buildFeature())
                .forEach(pro -> send(pro, message));
    }

    private void send(ActionProcessor pro, ActionMessage msg) {
        long rid = ObjectUtils.defaultIfNull(msg.getResource().getResourceId(), 0L).longValue();
        String actorName = buildActorName(pro, rid);
        ActorRef ref = ActorBuilder.build(ActionProcessorActor.class, actorName, pro);
        ref.tell(msg, null);
    }


    private String buildActorName(ActionProcessor pro, long rid) {
        String name = pro.getActorName();
        if (isShardEnable) {
            name += "-M" + (rid % mode);
        }
        return name;
    }

}
