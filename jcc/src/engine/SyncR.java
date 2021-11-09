package engine;

import meta.Meta;
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
    private final Set<SyncO> oprs = new HashSet<>();

    public SyncR() {
        Dojo.add(this);
    }

    public void add(SyncO opr) {
        oprs.add(opr);
    }

    public Get qry(MVar v) {
        if (!mp.containsKey(v)) mp.put(v, new Get(v));
        return mp.get(v);
    }

    @Override
    public void index(Set<MVar> s) {
        index();
    }

    @Override
    public void index() {
        for (SyncO o : oprs) o.index(mp.keySet());
    }

    @Override
    public void collect() {
        for (SyncO o : oprs) o.collect();
    }

    @Override
    public boolean isValid() {
        return !mp.isEmpty();
    }
}
