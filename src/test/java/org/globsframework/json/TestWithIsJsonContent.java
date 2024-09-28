package org.globsframework.json;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeLoaderFactory;
import org.globsframework.core.metamodel.fields.StringField;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.MutableGlob;
import org.globsframework.json.annottations.IsJsonContent_;
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
        @IsJsonContent_
        public static StringField value;

        static {
            GlobTypeLoaderFactory.create(TypeWithJsonAttr.class).load();
        }
    }
}
