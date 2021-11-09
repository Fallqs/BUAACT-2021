package engine;

import meta.Meta;
import meta.mcode.Put;
import meta.midt.MVar;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Synchronize Operation
 */
public class SyncO implements Index {
    private final Map<MVar, Put> mp = new HashMap<>();
    private final Set<MVar> reqs = new HashSet<>();
    private boolean valid = false;
    private final SyncO fa;
    private final SyncR rq;
    private int cnt = 0;

    public SyncO() {
        rq = Dojo.curReq;
        Dojo.add(fa = this);
    }

    public SyncO(SyncO fa) {
        rq = Dojo.curReq;
        this.fa = fa;
        Dojo.add(this);
    }

    public void upd(MVar v, Meta m) {
        if (!mp.containsKey(v)) mp.put(v, new Put(v, m));
        else mp.get(v).upd(m);
        // mp.get(v);
    }

    public Meta qry(MVar v) {
        if(!mp.containsKey(v)) return rq.qry(v);
        return mp.get(v).fr;
    }

    @Override
    public void index(Set<MVar> s) {
        reqs.addAll(s);
        if (cnt > 0) --cnt;
        if (cnt == 0) {
            mp.entrySet().removeIf(e -> !reqs.contains(e.getKey()));
            reqs.removeIf(e -> !mp.containsKey(e));
            valid = !mp.isEmpty();
            fa.index(reqs);
        }
    }

    @Override
    public void index() {
    }

    @Override
    public void collect() {
        if (cnt == 0) fa.collect();
        ++cnt;
    }

    public Set<Put> handle() {
        return (Set<Put>) mp.values();
    }

    @Override
    public boolean isValid() {
        return valid;
    }
}
