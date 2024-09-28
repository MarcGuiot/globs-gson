package org.globsframework.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.fields.*;
import org.globsframework.core.model.FieldSetter;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.MutableGlob;
import org.globsframework.json.annottations.IsJsonContent;
import org.globsframework.json.annottations.JsonAsObject;
import org.globsframework.json.annottations.JsonValueAsField;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class ReadJsonWithReaderFieldVisitor implements FieldVisitorWithTwoContext<FieldSetter, JsonReader> {

    public void visitInteger(IntegerField field, FieldSetter mutableGlob, JsonReader jsonReader) throws Exception {
        mutableGlob.set(field, jsonReader.nextInt());
    }

    public void visitIntegerArray(IntegerArrayField field, FieldSetter mutableGlob, JsonReader jsonReader) throws Exception {
        jsonReader.beginArray();
        int[] values = new int[16];
        int count = 0;
        while (jsonReader.peek() != JsonToken.END_ARRAY) {
            if (values.length == count) {
                values = Arrays.copyOf(values, values.length * 2);
            }
            values[count++] = jsonReader.nextInt();
        }
        jsonReader.endArray();
        mutableGlob.set(field, Arrays.copyOf(values, count));
    }

    public void visitDouble(DoubleField field, FieldSetter mutableGlob, JsonReader jsonReader) throws Exception {
        mutableGlob.set(field, jsonReader.nextDouble());
    }

    public void visitDoubleArray(DoubleArrayField field, FieldSetter mutableGlob, JsonReader jsonReader) throws Exception {
        jsonReader.beginArray();
        double[] values = new double[16];
        int count = 0;
        while (jsonReader.peek() != JsonToken.END_ARRAY) {
            if (values.length == count) {
                values = Arrays.copyOf(values, values.length * 2);
            }
            values[count++] = jsonReader.nextDouble();
        }
        jsonReader.endArray();
        mutableGlob.set(field, Arrays.copyOf(values, count));
    }

    public void visitString(StringField field, FieldSetter mutableGlob, JsonReader jsonReader) throws Exception {
        if (field.hasAnnotation(IsJsonContent.UNIQUE_KEY)) {
            JsonElement parse = JsonParser.parseReader(jsonReader);
            if (parse.isJsonArray()) {
                mutableGlob.set(field, GlobGSonDeserializer.GSON.toJson(parse.getAsJsonArray()));
            } else if (parse.isJsonObject()) {
                mutableGlob.set(field, GlobGSonDeserializer.GSON.toJson(parse.getAsJsonObject()));
            } else if (parse.isJsonPrimitive()) {
                mutableGlob.set(field, GlobGSonDeserializer.GSON.toJson(parse.getAsJsonPrimitive()));
            } else {
                throw new RuntimeException(parse.toString() + " not managed in " + field.getFullName());
            }
        } else {
            JsonToken peek = jsonReader.peek();
            switch (peek) {
                case STRING:
                case NUMBER:
                    mutableGlob.set(field, jsonReader.nextString());
                    break;
                case BOOLEAN:
                    mutableGlob.set(field, Boolean.toString(jsonReader.nextBoolean()));
                    break;
            }
        }
    }

    public void visitStringArray(StringArrayField field, FieldSetter mutableGlob, JsonReader jsonReader) throws Exception {
        jsonReader.beginArray();
        String[] values = new String[16];
        int count = 0;
        while (jsonReader.peek() != JsonToken.END_ARRAY) {
            if (values.length == count) {
                values = Arrays.copyOf(values, values.length * 2);
            }
            if (field.hasAnnotation(IsJsonContent.UNIQUE_KEY)) {
                JsonElement parse = JsonParser.parseReader(jsonReader);
                if (parse.isJsonArray()) {
                    values[count++] = GlobGSonDeserializer.GSON.toJson(parse.getAsJsonArray());
                } else if (parse.isJsonObject()) {
                    values[count++] = GlobGSonDeserializer.GSON.toJson(parse.getAsJsonObject());
                } else if (parse.isJsonPrimitive()) {
                    values[count++] = GlobGSonDeserializer.GSON.toJson(parse.getAsJsonPrimitive());
                } else {
                    throw new RuntimeException(parse + " not managed in " + field.getFullName());
                }
            } else {
                values[count++] = jsonReader.nextString();
            }
        }
        jsonReader.endArray();
        mutableGlob.set(field, Arrays.copyOf(values, count));
    }

    public void visitBoolean(BooleanField field, FieldSetter mutableGlob, JsonReader jsonReader) throws Exception {
        mutableGlob.set(field, jsonReader.nextBoolean());
    }

    public void visitBooleanArray(BooleanArrayField field, FieldSetter mutableGlob, JsonReader jsonReader) throws Exception {
        jsonReader.beginArray();
        boolean[] values = new boolean[16];
        int count = 0;
        while (jsonReader.peek() != JsonToken.END_ARRAY) {
            if (values.length == count) {
                values = Arrays.copyOf(values, values.length * 2);
            }
            values[count++] = jsonReader.nextBoolean();
        }
        jsonReader.endArray();
        mutableGlob.set(field, Arrays.copyOf(values, count));
    }

    public void visitBigDecimal(BigDecimalField field, FieldSetter mutableGlob, JsonReader jsonReader) throws Exception {
        mutableGlob.set(field, new BigDecimal(jsonReader.nextString()));
    }

    public void visitBigDecimalArray(BigDecimalArrayField field, FieldSetter mutableGlob, JsonReader jsonReader) throws Exception {
        jsonReader.beginArray();
        BigDecimal[] values = new BigDecimal[16];
        int count = 0;
        while (jsonReader.peek() != JsonToken.END_ARRAY) {
            if (values.length == count) {
                values = Arrays.copyOf(values, values.length * 2);
            }
            values[count++] = new BigDecimal(jsonReader.nextString());
        }
        jsonReader.endArray();
        mutableGlob.set(field, Arrays.copyOf(values, count));
    }

    public void visitLong(LongField field, FieldSetter mutableGlob, JsonReader jsonReader) throws Exception {
        mutableGlob.set(field, jsonReader.nextLong());
    }

    public void visitLongArray(LongArrayField field, FieldSetter mutableGlob, JsonReader jsonReader) throws Exception {
        jsonReader.beginArray();
        long[] values = new long[16];
        int count = 0;
        while (jsonReader.peek() != JsonToken.END_ARRAY) {
            if (values.length == count) {
                values = Arrays.copyOf(values, values.length * 2);
            }
            values[count++] = jsonReader.nextLong();
        }
        jsonReader.endArray();
        mutableGlob.set(field, Arrays.copyOf(values, count));
    }

    public void visitDate(DateField field, FieldSetter mutableGlob, JsonReader jsonReader) throws Exception {
        DateTimeFormatter dateConverter = GSonUtils.getCachedDateFormatter(field);
        mutableGlob.set(field, LocalDate.from(dateConverter.parse(jsonReader.nextString())));
    }

    // gestion a revoir

    public void visitDateTime(DateTimeField field, FieldSetter mutableGlob, JsonReader jsonReader) throws Exception {
        GSonUtils.FormaterForDateTime dateConverter = GSonUtils.getCachedDateTimeFormatter(field);
        String text = jsonReader.nextString();
//        if (field.hasAnnotation(JsonDateTimeFormatType.UNIQUE_KEY)) {
//            Glob annotation = field.getAnnotation(JsonDateTimeFormatType.UNIQUE_KEY);
//            String nullValue = annotation.get(JsonDateTimeFormatType.NULL_VALUE);
//            if (text.equals(nullValue) || "".equals(text)) {
//                return;
//            }
//            Boolean aBoolean = annotation.get(JsonDateTimeFormatType.AS_LOCAL);
//            if (aBoolean) {
//                mutableGlob.set(field, ZonedDateTime.of(LocalDateTime.from(dateConverter.parse(text)), ZoneId.systemDefault()));
//                return;
//            }
//        }
        mutableGlob.set(field, dateConverter.parse(text));
    }

    public void visitBlob(BlobField field, FieldSetter mutableGlob, JsonReader jsonReader) throws Exception {
        mutableGlob.set(field, Base64.getDecoder().decode(jsonReader.nextString()));
    }

    public void visitGlob(GlobField field, FieldSetter mutableGlob, JsonReader jsonReader) throws Exception {
        jsonReader.beginObject();
        mutableGlob.set(field, readField(jsonReader, field.getTargetType()));
        jsonReader.endObject();
    }

    public Glob readField(JsonReader jsonReader, GlobType type) throws IOException {
        return GlobGSonDeserializer.readFields(jsonReader, type);
    }

    public void visitGlobArray(GlobArrayField field, FieldSetter mutableGlob, JsonReader jsonReader) throws Exception {
        GlobType targetType = field.getTargetType();
        List<Glob> objs = new ArrayList<>();
        if (field.hasAnnotation(JsonAsObject.UNIQUE_KEY)) {
            Field fieldValueToUseAsName = targetType.findFieldWithAnnotation(JsonValueAsField.UNIQUE_KEY);
            if (fieldValueToUseAsName == null) {
                throw new RuntimeException("A field with " + JsonValueAsField.TYPE.getName() + " annotation is expected after " +
                        JsonAsObject.TYPE.getName());
            }
            StringField typedFieldToUseAsName = fieldValueToUseAsName.asStringField();
            jsonReader.beginObject();
            while (jsonReader.peek() != JsonToken.END_OBJECT) {
                MutableGlob newObj = targetType.instantiate();
                String value = jsonReader.nextName();
                newObj.set(typedFieldToUseAsName, value);
                jsonReader.beginObject();
                GlobGSonDeserializer.read(jsonReader, targetType, newObj);
                jsonReader.endObject();
                objs.add(newObj);
            }
        } else {
            jsonReader.beginArray();
            while (jsonReader.peek() != JsonToken.END_ARRAY) {
                jsonReader.beginObject();
                objs.add(readField(jsonReader, targetType));
                jsonReader.endObject();
            }
            jsonReader.endArray();
        }
        mutableGlob.set(field, objs.toArray(new Glob[0]));
    }

    public void visitUnionGlob(GlobUnionField field, FieldSetter mutableGlob, JsonReader jsonReader) throws Exception {
        jsonReader.beginObject();
        String name = jsonReader.nextName();
        jsonReader.beginObject();
        GlobType globType = field.getTargetType(name);
        mutableGlob.set(field, readField(jsonReader, globType));
        jsonReader.endObject();
        jsonReader.endObject();
    }

    public void visitUnionGlobArray(GlobArrayUnionField field, FieldSetter mutableGlob, JsonReader jsonReader) throws Exception {
        jsonReader.beginArray();
        Glob[] values = new Glob[16];
        int count = 0;
        while (jsonReader.peek() != JsonToken.END_ARRAY) {
            if (values.length == count) {
                values = Arrays.copyOf(values, values.length * 2);
            }
            jsonReader.beginObject();
            String name = jsonReader.nextName();
            jsonReader.beginObject();
            values[count++] = readField(jsonReader, field.getTargetType(name));
            jsonReader.endObject();
            jsonReader.endObject();
        }
        jsonReader.endArray();
        mutableGlob.set(field, Arrays.copyOf(values, count));
    }
}
