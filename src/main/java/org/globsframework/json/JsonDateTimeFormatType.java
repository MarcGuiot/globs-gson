package org.globsframework.json;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeLoaderFactory;
import org.globsframework.core.metamodel.annotations.GlobCreateFromAnnotation;
import org.globsframework.core.metamodel.annotations.InitUniqueKey;
import org.globsframework.core.metamodel.fields.BooleanField;
import org.globsframework.core.metamodel.fields.StringField;
import org.globsframework.core.model.Key;
import org.globsframework.json.annottations.JsonDateTimeFormatAnnotation;

public class JsonDateTimeFormatType {
    public static GlobType TYPE;

    public static StringField format;

    public static BooleanField strictIso8601;

    public static BooleanField useFastIso8601;

    public static BooleanField useLocalZone;

    public static StringField nullValue;

    @InitUniqueKey
    public static Key UNIQUE_KEY;

    static {
        GlobTypeLoaderFactory.create(JsonDateTimeFormatType.class, "jsonDateTimeFormat")
                .register(GlobCreateFromAnnotation.class, annotation -> TYPE.instantiate()
                        .set(format, ((JsonDateTimeFormatAnnotation) annotation).pattern())
                        .set(useLocalZone, ((JsonDateTimeFormatAnnotation) annotation).asLocal())
                        .set(nullValue, ((JsonDateTimeFormatAnnotation) annotation).nullValue())
                        .set(strictIso8601, ((JsonDateTimeFormatAnnotation) annotation).strictIso8601())
                )
                .load();
    }

}
