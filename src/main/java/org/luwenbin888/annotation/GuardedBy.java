package org.luwenbin888.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface GuardedBy {
    String value() default "";
}
