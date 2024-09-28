package org.globsframework.json.annottations;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.json.JsonDateTimeFormat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@java.lang.annotation.Target({ElementType.FIELD})
public @interface JsonDateTimeFormat_ {

    String pattern() default "";

    boolean strictIso8601() default false;

    boolean asLocal() default false;

    String nullValue() default "";

    GlobType TYPE = JsonDateTimeFormat.TYPE;
}
