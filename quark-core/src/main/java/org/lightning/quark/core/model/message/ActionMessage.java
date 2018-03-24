package org.lightning.quark.core.model.message;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by cook on 2018/3/19
 */
@Getter
@Setter
public class ActionMessage<T extends ResourceIdentifier> {

    /**
     *
     */
    private MessageFeature feature;

    /**
     *
     */
    private T resource;

}
