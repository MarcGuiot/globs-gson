package org.globsframework.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.globsframework.core.model.ChangeSet;

import java.io.IOException;

public class ChangeSetGsonAdapter extends TypeAdapter<ChangeSet> {

    public ChangeSetGsonAdapter() {
    }

    public void write(JsonWriter out, ChangeSet changeSet) throws IOException {
        ChangeValuesGsonAdapter.write(out, changeSet::safeAccept);
    }

    public ChangeSet read(JsonReader in) throws IOException {
        throw new RuntimeException("A changet is not readable, use PreChangeSet.");
    }
}


