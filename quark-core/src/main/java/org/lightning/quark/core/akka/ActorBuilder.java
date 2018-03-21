package org.lightning.quark.core.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * Created by cook on 2018/3/21
 */
public abstract class ActorBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ActorBuilder.class);

    private static final ActorSystem actorSystem = ActorSystem.create("quark");

    private static final Map<String, WeakReference<ActorRef>> refs = Maps.newConcurrentMap();

    /**
     *
     * @param actorClass
     * @param actorName
     * @param args
     * @return
     */
    public static ActorRef build(Class<? extends AbstractActor> actorClass, String actorName, Object... args) {
        String cacheName = actorClass.getName() + actorName;
        WeakReference<ActorRef> weak = refs.get(cacheName);
        if (weak != null) {
            return weak.get();
        }

        Props props;
        if (ArrayUtils.isNotEmpty(args)) {
            props = Props.create(actorClass, args);
        } else {
            props = Props.create(actorClass);
        }
        ActorRef ref = actorSystem.actorOf(props, actorName);

        refs.put(cacheName, new WeakReference<>(ref));

        logger.info("create actor#{}, actorName is {}", ref, actorName);

        return ref;
    }

}
