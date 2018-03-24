package org.lightning.quark.core.model.message;

/**
 * Created by cook on 2018/3/19
 */
public abstract class BaseResourceIdentifier<T extends Number> implements ResourceIdentifier<T> {

    private T resourceId;

    @Override
    public T getResourceId() {
        return resourceId;
    }

    public void setResourceId(T resourceId) {
        this.resourceId = resourceId;
    }
}
