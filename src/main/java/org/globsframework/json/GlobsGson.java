package org.globsframework.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeResolver;
import org.globsframework.core.model.ChangeSet;
import org.globsframework.core.model.Glob;

public class GlobsGson {

    public static final String KIND_NAME = "_kind";
    public static final String TYPE_NAME = "kind";
    public static final String FIELDS = "fields";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_NAME = "name";
    public static final String ANNOTATIONS = "annotations";
    public static final String INT_TYPE = "int";
    public static final String INT_ARRAY_TYPE = "intArray";
    public static final String DOUBLE_TYPE = "double";
    public static final String DOUBLE_ARRAY_TYPE = "doubleArray";
    public static final String STRING_TYPE = "string";
    public static final String STRING_ARRAY_TYPE = "stringArray";
    public static final String BOOLEAN_TYPE = "boolean";
    public static final String BOOLEAN_ARRAY_TYPE = "booleanArray";
    public static final String LONG_TYPE = "long";
    public static final String LONG_ARRAY_TYPE = "longArray";
    public static final String DATE_TYPE = "date";
    public static final String DATE_TIME_TYPE = "dateTime";
    public static final String BIG_DECIMAL_TYPE = "bigDecimal";
    public static final String BIG_DECIMAL_ARRAY_TYPE = "bigDecimalArray";
    public static final String BLOB_TYPE = "blob";
    public static final String GLOB_TYPE = "glob";
    public static final String GLOB_ARRAY_TYPE = "globArray";
    public static final String GLOB_TYPE_KIND = "kind";
    public static final String GLOB_UNION_TYPE = "globUnion";
    public static final String GLOB_UNION_ARRAY_TYPE = "globUnionArray";
    public static final String GLOB_UNION_KINDS = "kinds";

    private GlobsGson() {
    }

    // WARN : GlobTypeGsonAdapter is state full : it keep the
    static public GsonBuilder createBuilder(GlobTypeResolver globTypeResolver) {
        return createBuilder(globTypeResolver, true);
    }

    static public GsonBuilder createBuilder(GlobTypeResolver globTypeResolver, boolean ignoreUnknownAnnotation) {
        return new GsonBuilder()
//              .registerTypeHierarchyAdapter(GlobType.class, new GlobTypeGsonAdapter(false, globTypeResolver))
                .registerTypeHierarchyAdapter(ChangeSet.class, new ChangeSetGsonAdapter())
                .registerTypeHierarchyAdapter(GlobType.class, new GlobTypeArrayGsonAdapter(false, globTypeResolver, ignoreUnknownAnnotation))
                .registerTypeHierarchyAdapter(GlobTypeSet.class, new GlobTypeSetAdapter(false, globTypeResolver, ignoreUnknownAnnotation))
                .registerTypeHierarchyAdapter(Glob.class, new GlobGsonAdapter(globTypeResolver))
//                .registerTypeHierarchyAdapter(Key.class, new KeyGsonAdapter(globTypeResolver)) // not possible because AbstractGlob inherit from Key...
                .registerTypeHierarchyAdapter(PreChangeSet.class, new PreChangeSetGsonAdapter(globTypeResolver))
                ;
    }


    public static Gson create(GlobTypeResolver globTypeResolver) {
        return createBuilder(globTypeResolver, false).create();
    }
}
