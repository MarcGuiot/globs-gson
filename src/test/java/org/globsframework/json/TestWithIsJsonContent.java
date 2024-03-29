package org.globsframework.json;

import org.globsframework.json.annottations.IsJsonContentAnnotation;
import org.globsframework.metamodel.GlobType;
import org.globsframework.metamodel.GlobTypeLoaderFactory;
import org.globsframework.metamodel.fields.StringField;
import org.globsframework.model.Glob;
import org.globsframework.model.MutableGlob;
import org.junit.Assert;
import org.junit.Test;

public class TestWithIsJsonContent {

    @Test
    public void withComplexType() {
        check("false", "{\"value\":false}");
        check("12", "{\"value\":12}");
        check("\"false\"", "{\"value\":\"false\"}");
        check("{\"a\":true}", "{\"value\":{\"a\":true}}");
        check("[{\"a\":true}]", "{\"value\":[{\"a\":true}]}");
    }

    private void check(String value, String expected) {
        MutableGlob v = TypeWithJsonAttr.TYPE.instantiate().set(TypeWithJsonAttr.value, value);
        Assert.assertEquals(expected, GSonUtils.encode(v, false));
        Glob r = GSonUtils.decode(GSonUtils.encode(v, false), TypeWithJsonAttr.TYPE);
        Assert.assertTrue(TypeWithJsonAttr.value.valueEqual(v.get(TypeWithJsonAttr.value), r.get(TypeWithJsonAttr.value)));
    }


    public static class TypeWithJsonAttr {
        public static GlobType TYPE;
        @IsJsonContentAnnotation
        public static StringField value;
        static {
            GlobTypeLoaderFactory.create(TypeWithJsonAttr.class).load();
        }
    }
}
