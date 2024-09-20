package org.globsframework.json.annottations;

import org.globsframework.core.metamodel.GlobModel;
import org.globsframework.core.metamodel.GlobTypeResolver;
import org.globsframework.core.metamodel.impl.DefaultGlobModel;
import org.globsframework.json.JsonDateTimeFormatType;

public class AllAnnotations {
    public final static GlobModel MODEL =
            new DefaultGlobModel(IsJsonContentType.TYPE, JsonDateTimeFormatType.TYPE, JsonDateFormatType.TYPE, UnknownAnnotation.TYPE,
                    JsonHidValue.TYPE, JsonValueAsFieldType.TYPE, JsonAsObjectType.TYPE);

    public final static GlobTypeResolver RESOLVER = GlobTypeResolver.chain(org.globsframework.core.metamodel.annotations.AllAnnotations.MODEL::findType,
            MODEL::findType);
}
