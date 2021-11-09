package engine;

import meta.Meta;
import meta.midt.MVar;

import java.util.ArrayList;

public class Dojo {
    private static final ArrayList<SyncR> reqs = new ArrayList<>();
    private static final ArrayList<SyncO> oprs = new ArrayList<>();
    public static SyncR curReq;
    public static SyncO curOpr;

    public static void add(Index ix) {
        if (ix instanceof SyncR) {
            reqs.add(curReq = (SyncR) ix);
        } else {
            oprs.add(curOpr = (SyncO) ix);
        }
    }

    public static Meta qry(MVar v) {
        return curOpr.qry(v);
    }

    public static void upd(MVar v, Meta m) {
        curOpr.upd(v, m);
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
