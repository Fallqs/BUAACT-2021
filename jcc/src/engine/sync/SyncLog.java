package engine.sync;

import meta.Meta;

public class SyncLog {
    public final Meta key, value;

    public SyncLog(Meta key, Meta value) {
        this.key = key;
        this.value = value;
    }
}
