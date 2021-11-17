package engine.sync;

import engine.Dojo;
import engine.Index;
import meta.Meta;
import meta.mcode.Call;
import meta.mcode.Get;
import meta.mcode.Phi;
import meta.mcode.Psi;
import meta.mcode.Put;
import meta.mcode.Ret;
import meta.midt.MFunc;
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
    private final Map<MVar, Meta> mp = new HashMap<>();
    private final Set<MVar> reqs = new HashSet<>(), spreads = new HashSet<>();
    public final ArrayList<SyncR> legendH = new ArrayList<>();
    public final ArrayList<SyncR> legendL = new ArrayList<>();
    public final Set<Meta> alive = new HashSet<>();
    public final Map<SyncR, ArrayList<Meta>> psi = new HashMap<>();
    public final SyncB blk;
    public final SyncR rq;
    private int cnt = 0;
    public Meta end;

    public MFunc func;

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
        legendH.add(req);
    }

    public void addLegendL(SyncR req) {
        legendL.add(req);
    }

    public void upd(MVar v, Meta m) {
        if (end != null) return;
        if (m instanceof Call) m = new Get(v).asLegend(m);
        mp.put(v, m);
        if (Dojo.curFunc != null) Dojo.curFunc.write(this, v);
    }

    public Meta qry(MVar v) {
        if (!mp.containsKey(v)) mp.put(v, rq.qry(v));
        return mp.get(v);
    }

    public Collection<Put> save() {
        List<Put> ret = new ArrayList<>();
        if (end == null) for (Map.Entry<MVar, Meta> e : mp.entrySet()) ret.add(new Put(e.getKey(), e.getValue()));
        return ret;
    }

    public void flush(Meta m) {
        if (end == null) mp.entrySet().forEach(e -> e.setValue(new Get(e.getKey()).asLegend(m)));
    }

    private int indexCnt = 0;

    @Override
    public void setFunc(MFunc f) {
        if (func == null) {
            func = f;
            for (Index i : legendH) i.setFunc(f);
        }
    }

    @Override
    public void flushCnt() {
        if (indexCnt != 0) {
            indexCnt = 0;
            for (Index i : legendH) i.flushCnt();
            blk.req.flushCnt();
        }
    }

    @Override
    public void indexOpr(Map<MVar, Meta> mp) {
        if (indexCnt == -1) return;
        indexCnt = -1;
        for (Map.Entry<MVar, Meta> e : mp.entrySet())
            if (!this.mp.containsKey(e.getKey())) this.mp.put(e.getKey(), e.getValue());
        for (SyncR req : legendH) req.indexOpr(this.mp);
    }

    @Override
    public void indexPhi() {
        if (indexCnt == -1) return;
        indexCnt = -1;
        for (Index i : legendH) i.indexPhi();
    }

    @Override
    public void indexMeta(Set<Meta> s) {
        alive.addAll(s);
        if (indexCnt < 0) return;
        if (++indexCnt >= legendH.size()) {
            indexCnt = -1;
            Set<MVar> vars = new HashSet<>();
            for (SyncR req : legendL) {
                psi.put(req, new ArrayList<>());
                for (Map.Entry<MVar, Meta> e : req.mp.entrySet()) {
                    if (!(e.getValue() instanceof Phi)) continue;
                    if (mp.containsKey(e.getKey())) {
                        vars.add(e.getKey());
                        psi.get(req).add(new Psi(mp.get(e.getKey()), e.getValue()));
                    }
                }
            }
            for (Map.Entry<MVar, Meta> e : mp.entrySet()) if (vars.contains(e.getKey())) e.getValue().valid = true;
            List<Meta> soup = blk.ms;
            for (int i = soup.size() - 1; i > -1; --i) {
                Meta m = soup.get(i);
                if (!alive.contains(m) && !m.valid) continue;
                alive.remove(m);

            }
            blk.req.indexMeta(alive);
        }
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
        for (Meta i : mp.values()) end.asLegend(i);
    }

    public void setEnd() {
        if (end == null) setEnd(new Ret());
    }
}
