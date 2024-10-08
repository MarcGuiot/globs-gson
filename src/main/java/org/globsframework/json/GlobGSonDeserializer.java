package org.globsframework.json;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeResolver;
import org.globsframework.core.metamodel.fields.Field;
import org.globsframework.core.model.*;
import org.globsframework.json.annottations.UnknownAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GlobGSonDeserializer {
    public static final Gson GSON;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        GSON = gsonBuilder.create();
    }

    public static final GSonVisitor G_SON_VISITOR = new GSonVisitor() {
        Glob readGlob(JsonObject jsonObject, GlobType globType) {
            return GlobGSonDeserializer.readGlob(jsonObject, globType);
        }
    };
    private static final ReadJsonWithReaderFieldVisitor fieldVisitor = new ReadJsonWithReaderFieldVisitor();
    private static Logger LOGGER = LoggerFactory.getLogger(GlobGSonDeserializer.class);

    public GlobGSonDeserializer() {
    }

    public static Glob deserialize(JsonElement json, GlobTypeResolver globTypeResolver, boolean ignoreUnknownAnnotation) throws JsonParseException {
        if (json == null || json instanceof JsonNull) {
            return null;
        }
        MutableGlob instantiate = null;
        try {
            JsonObject jsonObject = (JsonObject) json;
            String type = jsonObject.get(GlobsGson.KIND_NAME).getAsString();
            if (type.equals(UnknownAnnotation.TYPE.getName())) {
                MutableGlob mutableGlob = readGlob(jsonObject, UnknownAnnotation.TYPE);
                return deserialize(JsonParser.parseReader(new StringReader(mutableGlob.get(UnknownAnnotation.CONTENT))), globTypeResolver, ignoreUnknownAnnotation);
            }
            GlobType globType = ignoreUnknownAnnotation ? globTypeResolver.findType(type) : globTypeResolver.getType(type);
            if (globType == null) {
                LOGGER.debug("Unknown annotation " + type);
                return UnknownAnnotation.TYPE.instantiate()
                        .set(UnknownAnnotation.uuid, UUID.randomUUID().toString())
                        .set(UnknownAnnotation.CONTENT, GSON.toJson(json));
            }
            instantiate = readGlob(jsonObject, globType);
        } catch (Exception e) {
            LOGGER.error("Fail to parse : " + GSON.toJson(json), e);
            throw e;
        }
        return instantiate;
    }

    public static MutableGlob readGlob(JsonObject jsonObject, GlobType globType) {
        MutableGlob instantiate;
        instantiate = globType.instantiate();
        for (Field field : globType.getFields()) {
            JsonElement jsonElement = jsonObject.get(field.getName());
            if (jsonElement != null) {
                field.safeAccept(G_SON_VISITOR, jsonElement, instantiate);
            }
        }
        return instantiate;
    }

    public static Glob readFields(JsonReader in, GlobType globType) throws IOException {
        MutableGlob instantiate = globType.instantiate();
        read(in, globType, instantiate);
        return instantiate;
    }

    public static void read(JsonReader in, GlobType globType, FieldSetter instantiate) throws IOException {
        while (in.hasNext() && in.peek() == JsonToken.NAME) {
            String name = in.nextName();
            Field field = globType.findField(name);
            if (field != null) {
                if (in.peek() != JsonToken.NULL) {
                    field.safeAccept(fieldVisitor, instantiate, in);
                } else {
                    in.skipValue();
                    instantiate.setValue(field, null);
                }
            } else {
                in.skipValue();
            }
        }
    }

    public static Glob read(JsonReader in, GlobTypeResolver resolver) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            return null;
        }
        in.beginObject();
        if (in.hasNext() && in.peek() != JsonToken.END_OBJECT) {
            String name = in.nextName();
            if (name.equalsIgnoreCase(GlobsGson.KIND_NAME)) {
                String kind = in.nextString();
                Glob glob = readFields(in, resolver.getType(kind));
                in.endObject();
                return glob;
            } else {
                return readFieldByField(in, name, resolver);
            }
        }
        return null;
    }

    public static Key readKey(JsonReader in, GlobTypeResolver resolver) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            return null;
        }
        in.beginObject();
        if (in.hasNext() && in.peek() != JsonToken.END_OBJECT) {
            String name = in.nextName();
            if (name.equalsIgnoreCase(GlobsGson.KIND_NAME)) {
                String kind = in.nextString();
                GlobType type = resolver.getType(kind);
                KeyBuilder keyBuilder = KeyBuilder.init(type);
                read(in, type, keyBuilder);
                in.endObject();
                return keyBuilder.get();
            } else {
                return readKeyFieldByField(in, name, resolver);
            }
        }
        return null;
    }

    private static Glob readFieldByField(JsonReader in, String name, GlobTypeResolver resolver) throws IOException {
        Map<String, JsonElement> values = new HashMap<>();
        values.put(name, JsonParser.parseReader(in));
        while (in.peek() != JsonToken.END_OBJECT) {
            values.put(in.nextName(), JsonParser.parseReader(in));
        }
        in.endObject();
        JsonElement kindElement = values.get(GlobsGson.KIND_NAME);
        if (kindElement == null) {
            throw new RuntimeException("kind not found in " + values);
        }
        GlobType type = resolver.getType(kindElement.getAsString());
        MutableGlob instantiate = type.instantiate();
        for (Map.Entry<String, JsonElement> stringJsonElementEntry : values.entrySet()) {
            Field field = type.findField(stringJsonElementEntry.getKey());
            if (field != null) {
                field.safeAccept(G_SON_VISITOR, stringJsonElementEntry.getValue(), instantiate);
            }
        }
        return instantiate;
    }

    private static Key readKeyFieldByField(JsonReader in, String name, GlobTypeResolver resolver) throws IOException {
        Map<String, JsonElement> values = new HashMap<>();
        values.put(name, JsonParser.parseReader(in));
        while (in.peek() != JsonToken.END_OBJECT) {
            values.put(in.nextName(), JsonParser.parseReader(in));
        }
        in.endObject();
        JsonElement kindElement = values.get(GlobsGson.KIND_NAME);
        if (kindElement == null) {
            throw new RuntimeException("kind not found in " + values);
        }
        GlobType type = resolver.getType(kindElement.getAsString());
        KeyBuilder instantiate = KeyBuilder.init(type);
        for (Map.Entry<String, JsonElement> stringJsonElementEntry : values.entrySet()) {
            Field field = type.findField(stringJsonElementEntry.getKey());
            if (field != null) {
                field.safeAccept(G_SON_VISITOR, stringJsonElementEntry.getValue(), instantiate);
            }
        }
        return instantiate.get();
    }

}
