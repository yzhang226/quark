package org.lightning.quark.core.dispatch;

import akka.actor.ActorRef;
import org.lightning.quark.core.akka.ActorBuilder;
import org.lightning.quark.core.model.message.ActionMessage;

/**
 * Created by cook on 2018/3/19
 */
public class ActionMessageDispatcher {

    private boolean isShardEnable;

    private int mode;

    private ActorRef dispatchActor;

    public ActionMessageDispatcher(boolean isShardEnable, int mode) {
        dispatchActor = ActorBuilder.build(ActionMessageDispatchActor.class, "dispatch", isShardEnable, mode);
    }

    /**
     *
     * @param message
     */
    public void dispatch(ActionMessage message) {
        dispatchActor.tell(message, null);
    }

}
