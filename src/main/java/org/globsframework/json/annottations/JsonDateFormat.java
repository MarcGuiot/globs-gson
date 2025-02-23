package org.globsframework.json.annottations;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeBuilder;
import org.globsframework.core.metamodel.GlobTypeBuilderFactory;
import org.globsframework.core.metamodel.annotations.GlobCreateFromAnnotation;
import org.globsframework.core.metamodel.annotations.InitUniqueKey;
import org.globsframework.core.metamodel.fields.StringField;
import org.globsframework.core.model.Key;
import org.globsframework.core.model.KeyBuilder;

public class JsonDateFormat {
    public static final GlobType TYPE;

    public static final StringField FORMAT;

    @InitUniqueKey
    public static final Key UNIQUE_KEY;

    static {
        GlobTypeBuilder typeBuilder = GlobTypeBuilderFactory.create("JsonDateFormat");
        TYPE = typeBuilder.unCompleteType();
        FORMAT = typeBuilder.declareStringField("format");
        typeBuilder.complete();
        typeBuilder.register(GlobCreateFromAnnotation.class, annotation -> TYPE.instantiate().set(FORMAT, ((JsonDateFormat_) annotation).value()));
        UNIQUE_KEY = KeyBuilder.newEmptyKey(TYPE);
//        GlobTypeLoaderFactory.create(JsonDateFormat.class, "JsonDateFormat")
//                .register(GlobCreateFromAnnotation.class, annotation -> TYPE.instantiate().set(FORMAT, ((JsonDateFormat_) annotation).value()))
//                .load();
    }

}
