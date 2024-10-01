package org.globsframework.json.annottations;

import org.globsframework.core.metamodel.GlobModel;
import org.globsframework.core.metamodel.GlobTypeResolver;
import org.globsframework.core.metamodel.impl.DefaultGlobModel;

public class AllAnnotations {
    public final static GlobModel MODEL =
            new DefaultGlobModel(IsJsonContent.TYPE, JsonDateTimeFormat.TYPE, JsonDateFormat.TYPE, UnknownAnnotation.TYPE,
                    JsonHideValue.TYPE, JsonValueAsField.TYPE, JsonAsObject.TYPE);

    public final static GlobTypeResolver RESOLVER = GlobTypeResolver.chain(org.globsframework.core.metamodel.annotations.AllAnnotations.MODEL::findType,
            MODEL::findType);
}
