package engine;

import java.util.ArrayList;
import java.util.List;

public class Dojo {
    private static final ArrayList<SyncR> reqs = new ArrayList<>();
    private static final ArrayList<SyncO> oprs = new ArrayList<>();

    public static void add(Index ix) {
        if (ix instanceof SyncR) {
            reqs.add((SyncR) ix);
        } else {
            oprs.add((SyncO) ix);
        }
    }

    public static void index() {
        reqs.removeIf(e -> !e.isValid());
        for (SyncR r : reqs) r.collect();
        for (SyncR r : reqs) r.index();
        oprs.removeIf(e -> !e.isValid());
    }

    public static void translate() {
        for (SyncO o : oprs) o.handle().forEach(Interpreter::handle);
    }
}
