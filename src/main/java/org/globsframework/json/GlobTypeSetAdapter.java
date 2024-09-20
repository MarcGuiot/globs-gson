package org.globsframework.json;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeResolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class GlobTypeSetAdapter extends TypeAdapter<GlobTypeSet> {
    GlobTypeArrayGsonAdapter globTypeArrayGsonAdapter;
    private boolean forceSort;
    private GlobTypeResolver globTypeResolver;
    private boolean ignoreUnknownAnnotation;

    public GlobTypeSetAdapter(boolean forceSort, GlobTypeResolver globTypeResolver, boolean ignoreUnknownAnnotation) {
        this.forceSort = forceSort;
        this.globTypeResolver = globTypeResolver;
        this.ignoreUnknownAnnotation = ignoreUnknownAnnotation;
        globTypeArrayGsonAdapter = new GlobTypeArrayGsonAdapter(forceSort, globTypeResolver, ignoreUnknownAnnotation);
    }

    public void write(JsonWriter out, GlobTypeSet value) throws IOException {
        out.beginArray();
        for (GlobType globType : value.globType) {
            globTypeArrayGsonAdapter.write(out, globType);
        }
        out.endArray();
    }

    public GlobTypeSet read(JsonReader in) throws IOException {
        JsonElement root = JsonParser.parseReader(in);
        if (root == null || root == JsonNull.INSTANCE) {
            return new GlobTypeSet(new GlobType[0]);
        }
        if (!root.isJsonArray()) {
            throw new RuntimeException("array expected got " + root.toString());
        }
        JsonArray asJsonArray = root.getAsJsonArray();
        Map<String, JsonObject> typesToRead = new LinkedHashMap<>();
        for (JsonElement jsonElement : asJsonArray) {
            JsonObject jsonType = jsonElement.getAsJsonObject();
            JsonElement jsonKind = jsonType.get(GlobsGson.TYPE_NAME);
            if (jsonKind == null) {
                throw new RuntimeException("Bug missing " + GlobsGson.TYPE_NAME + " attribute in " + asJsonArray.toString());
            }
            String kind = jsonKind.getAsString();
            typesToRead.put(kind, jsonType);
        }
        Resolver resolver = new Resolver(globTypeResolver, typesToRead, ignoreUnknownAnnotation);
        typesToRead.forEach((key, value) -> resolver.findType(key));
        return new GlobTypeSet(typesToRead.keySet().stream().map(resolver::findType).toArray(GlobType[]::new));
    }

    static class Resolver implements GlobTypeResolver {
        private GlobTypeResolver globTypeResolver;
        final Map<String, JsonObject> typesToRead;
        final Map<String, GlobType> readTypes = new HashMap<>();
        private final GlobTypeGsonDeserializer globTypeGsonDeserializer;

        Resolver(GlobTypeResolver globTypeResolver, Map<String, JsonObject> typesToRead, boolean ignoreUnknownAnnotation) {
            this.globTypeResolver = globTypeResolver;
            this.typesToRead = typesToRead;
            globTypeGsonDeserializer = new GlobTypeGsonDeserializer(new GlobGSonDeserializer(), this, ignoreUnknownAnnotation);
        }

        public GlobType findType(String name) {
            GlobType globType1 = globTypeResolver.findType(name);
            if (globType1 != null) {
                return globType1;
            }
            JsonObject jsonObject = typesToRead.get(name);
            if (jsonObject == null) {
                return null;
            }
            GlobType globType = readTypes.get(name);
            if (globType != null) {
                return globType;
            }
            GlobType readType = globTypeGsonDeserializer.deserialize(jsonObject);
            readTypes.put(readType.getName(), readType);
            return readType;
        }
    }

}
