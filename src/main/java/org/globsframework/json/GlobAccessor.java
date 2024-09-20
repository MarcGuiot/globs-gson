package org.globsframework.json;

import org.globsframework.core.model.Glob;
import org.globsframework.core.model.Key;

public interface GlobAccessor {
    Glob get(Key key);
}
