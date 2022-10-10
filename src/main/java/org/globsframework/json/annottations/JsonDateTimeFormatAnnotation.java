package org.globsframework.json.annottations;

import org.globsframework.json.JsonDateTimeFormatType;
import org.globsframework.metamodel.GlobType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@java.lang.annotation.Target({ElementType.FIELD})
public @interface JsonDateTimeFormatAnnotation {

    String pattern() default "";

    boolean strictIso8601() default false;

    boolean asLocal() default false;

    String nullValue() default "";

    GlobType TYPE = JsonDateTimeFormatType.TYPE;
}
