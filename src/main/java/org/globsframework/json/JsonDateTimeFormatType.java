package org.globsframework.json;

import org.globsframework.json.annottations.JsonDateTimeFormatAnnotation;
import org.globsframework.metamodel.GlobType;
import org.globsframework.metamodel.GlobTypeLoaderFactory;
import org.globsframework.metamodel.annotations.GlobCreateFromAnnotation;
import org.globsframework.metamodel.annotations.InitUniqueKey;
import org.globsframework.metamodel.fields.BooleanField;
import org.globsframework.metamodel.fields.StringField;
import org.globsframework.model.Key;

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
