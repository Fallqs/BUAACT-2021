package engine;

import meta.Meta;
import meta.mcode.Call;
import meta.mcode.Get;
import meta.mcode.Put;
import meta.mcode.Ret;
import meta.midt.MVar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Synchronize Operation
 */
public class SyncO implements Index {
    private final Map<MVar, Item> mp = new HashMap<>();
    private final Set<MVar> reqs = new HashSet<>(), spreads = new HashSet<>();
    public final ArrayList<SyncR> legends = new ArrayList<>();
    public final SyncB blk;
    public final SyncR rq;
    private int cnt = 0;
    private Meta end;

    public SyncO() {
        rq = Dojo.curReq;
        blk = null;
        Dojo.add(this);
    }

    public SyncO(SyncB blk) {
        rq = Dojo.curReq;
        this.blk = blk;
        Dojo.add(this);
    }

    private static class Item {
        public Meta fr;
        public final MVar v;

        public Item(MVar v, Meta m) {
            this.v = v;
            this.fr = m;
        }

        public void upd(Meta m) {
            this.fr = m;
        }

        public Put translate() {
            return new Put(v, fr);
        }
    }

    public void addLegend(SyncR req) {
        legends.add(req);
    }

    public void upd(MVar v, Meta m) {
        if (end != null) return;
        if (m instanceof Call) m = new Get(v).asLegend(m);
        if (!mp.containsKey(v)) mp.put(v, new Item(v, m));
        else mp.get(v).upd(m);
        if (Dojo.curFunc != null) Dojo.curFunc.write(this, v);
    }

    public Meta qry(MVar v) {
        if (!mp.containsKey(v)) return rq.qry(v);
        return mp.get(v).fr;
    }

    public Collection<Put> save() {
        List<Put> ret = new ArrayList<>();
        if (end == null) for (Item i : mp.values()) ret.add(i.translate());
        return ret;
    }

    public void flush(Meta m) {
        if (end == null) mp.forEach((key, value) -> value.upd(new Get(key).asLegend(m)));
    }

    @Override
    public void index(Set<MVar> s) {
        int sz = reqs.size();
        reqs.addAll(s);
        spreads.addAll(s);
        spreads.removeAll(mp.keySet());
        if (sz != reqs.size()) rq.index(spreads);
    }

    public void slim() {
        mp.entrySet().removeIf(e -> !reqs.contains(e.getKey()));
        reqs.removeIf(e -> !mp.containsKey(e));
    }

    @Override
    public void collect() {
        if (cnt == 0) blk.req.collect();
        ++cnt;
    }

    public Collection<Put> handle() {
        return save();
    }

    public void setEnd(Meta m) {
        if (end != null) return;
        end = m;
        for (Item i : mp.values()) {
            i.upd(i.translate());
            end.asLegend(i.fr);
        }
    }

    public void setEnd() {
        if (end == null) setEnd(new Ret());
    }
}
