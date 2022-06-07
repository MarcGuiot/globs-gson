package org.globsframework.json;

import com.google.gson.stream.JsonWriter;
import org.globsframework.json.annottations.JsonHidValue;
import org.globsframework.metamodel.fields.FieldValueVisitor;
import org.globsframework.metamodel.fields.StringField;

public class JsonFieldValueVisitorHideSensitiveData extends JsonFieldValueVisitor {
    public JsonFieldValueVisitorHideSensitiveData(JsonWriter jsonWriter) {
        super(jsonWriter);
    }

    @Override
    public void visitString(StringField field, String value) throws Exception {
        if (field.hasAnnotation(JsonHidValue.UNIQUE_KEY)) {
            super.visitString(field, value != null ? "XXXX" : null);
        }
        else {
            super.visitString(field, value);
        }
    }
}
