package org.globsframework.json.annottations;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeLoaderFactory;
import org.globsframework.core.metamodel.annotations.KeyField_;
import org.globsframework.core.metamodel.fields.StringField;

public class UnknownAnnotation {
    public static GlobType TYPE;

    @KeyField_
    public static StringField uuid;

    @IsJsonContent_
    public static StringField CONTENT;

    static {
        GlobTypeLoaderFactory.create(UnknownAnnotation.class, "UnknownAnnotation").load();
    }
}
