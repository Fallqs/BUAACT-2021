package engine.sync;

import engine.Dojo;
import engine.Index;
import meta.mcode.Get;
import meta.midt.MVar;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Synchronize Requirement
 */
public class SyncR implements Index {
    private final Map<MVar, Get> mp = new HashMap<>();
    private final Set<MVar> reqs = new HashSet<>();
    private final Set<Index> oprs = new HashSet<>();
    public final SyncB blk;

    public SyncR() {
        blk = null;
        Dojo.add(this);
    }

    public SyncR(SyncB blk) {
        this.blk = blk;
        Dojo.add(this);
    }

    public void add(SyncO opr) {
        oprs.add(opr);
        opr.addLegend(this);
    }

    public Get qry(MVar v) {
        if (!reqs.contains(v)) {
            mp.put(v, new Get(v));
            reqs.add(v);
        }
        return mp.get(v);
    }

    @Override
    public void index(Set<MVar> s) {
        int sz = reqs.size();
        reqs.addAll(s);
        if (sz != reqs.size()) for (Index o : oprs) o.index(reqs);
    }

    public void index() {
        for (Index o : oprs) o.index(mp.keySet());
    }

    @Override
    public void collect() {
        for (Index o : oprs) o.collect();
    }

    @Override
    public String toString() {
        return "entry" + blk.id;
    }
}
