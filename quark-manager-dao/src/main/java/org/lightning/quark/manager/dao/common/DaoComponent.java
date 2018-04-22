package org.lightning.quark.manager.dao.common;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * Created by cook on 2018/4/23
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(TYPE)
@Inherited
public @interface DaoComponent {

}
