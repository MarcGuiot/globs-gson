package org.globsframework.json.annottations;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeLoaderFactory;
import org.globsframework.core.metamodel.annotations.GlobCreateFromAnnotation;
import org.globsframework.core.metamodel.annotations.InitUniqueKey;
import org.globsframework.core.metamodel.fields.BooleanField;
import org.globsframework.core.metamodel.fields.StringField;
import org.globsframework.core.model.Key;

public class JsonDateTimeFormat {
    public static GlobType TYPE;

    public static StringField format;

    public static BooleanField strictIso8601;

    public static BooleanField useFastIso8601;

    public static BooleanField useLocalZone;

    public static StringField nullValue;

    @InitUniqueKey
    public static Key UNIQUE_KEY;

    static {
        GlobTypeLoaderFactory.create(JsonDateTimeFormat.class, "JsonDateTimeFormat")
                .register(GlobCreateFromAnnotation.class, annotation -> TYPE.instantiate()
                        .set(format, ((JsonDateTimeFormat_) annotation).pattern())
                        .set(useLocalZone, ((JsonDateTimeFormat_) annotation).asLocal())
                        .set(nullValue, ((JsonDateTimeFormat_) annotation).nullValue())
                        .set(strictIso8601, ((JsonDateTimeFormat_) annotation).strictIso8601())
                )
                .load();
    }

}
