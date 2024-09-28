package org.globsframework.json.annottations;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeLoaderFactory;
import org.globsframework.core.metamodel.annotations.GlobCreateFromAnnotation;
import org.globsframework.core.metamodel.annotations.InitUniqueKey;
import org.globsframework.core.metamodel.fields.StringField;
import org.globsframework.core.model.Key;

public class JsonDateFormat {
    public static GlobType TYPE;

    public static StringField FORMAT;

    @InitUniqueKey
    public static Key UNIQUE_KEY;

    static {
        GlobTypeLoaderFactory.create(JsonDateFormat.class, "JsonDateFormat")
                .register(GlobCreateFromAnnotation.class, annotation -> TYPE.instantiate().set(FORMAT, ((JsonDateFormat_) annotation).value()))
                .load();
    }

}
