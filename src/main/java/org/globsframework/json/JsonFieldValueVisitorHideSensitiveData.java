package org.globsframework.json;

import com.google.gson.stream.JsonWriter;
import org.globsframework.core.metamodel.fields.StringField;
import org.globsframework.json.annottations.JsonHideValue;

public class JsonFieldValueVisitorHideSensitiveData extends JsonFieldValueVisitor {
    public JsonFieldValueVisitorHideSensitiveData(JsonWriter jsonWriter) {
        super(jsonWriter);
    }

    @Override
    public void visitString(StringField field, String value) throws Exception {
        if (field.hasAnnotation(JsonHideValue.UNIQUE_KEY)) {
            super.visitString(field, value != null ? "••••" : null);
        } else {
            super.visitString(field, value);
        }
    }
}
