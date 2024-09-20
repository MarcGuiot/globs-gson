package org.globsframework.json;

import org.globsframework.core.model.ChangeSet;

public interface PreChangeSet {
    ChangeSet resolve(GlobAccessor globAccessor);
}
