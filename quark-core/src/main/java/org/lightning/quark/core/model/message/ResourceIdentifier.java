package org.lightning.quark.core.model.message;

/**
 * Created by cook on 2018/3/19
 */
public interface ResourceIdentifier<T extends Number> {

    /**
     * 资源标示
     * @return
     */
    T getResourceId();

}
