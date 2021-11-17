package engine.sync;

import engine.Dojo;
import engine.Index;
import meta.Meta;
import meta.mcode.Phi;
import meta.midt.MFunc;
import meta.midt.MVar;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Synchronize Requirement
 */
public class SyncR implements Index {
    public final Map<MVar, Meta> mp = new HashMap<>();
    private final Set<MVar> reqs = new HashSet<>();
    private final Set<Index> oprH = new HashSet<>();
    private final Set<Index> oprL = new HashSet<>();
    public final SyncB blk;

    public MFunc func;

    public SyncR() {
        blk = null;
        Dojo.add(this);
    }

    public SyncR(SyncB blk) {
        this.blk = blk;
        Dojo.add(this);
    }

    public void add(SyncO opr) {
        oprH.add(opr);
        opr.addLegend(this);
    }

    public void addL(SyncO opr) {
        oprL.add(opr);
        opr.addLegendL(this);
    }

    public Meta qry(MVar v) {
        if (!mp.containsKey(v)) {
            mp.put(v, new Phi(v));
            reqs.add(v);
        }
        return mp.get(v);
    }

    private int indexCnt = 0;

    @Override
    public void setFunc(MFunc f) {
        if (func == null) {
            func = f;
            blk.opr.setFunc(f);
        }
    }

    @Override
    public void flushCnt() {
        if (indexCnt != 0) {
            indexCnt = 0;
            for (Index i : oprH) i.flushCnt();
            this.blk.opr.flushCnt();
        }
    }

    @Override
    public void indexOpr(Map<MVar, Meta> mp) {
        for (Map.Entry<MVar, Meta> e : mp.entrySet()) {
            Phi p = (Phi) this.mp.get(e.getKey());
            if (p == null) this.mp.put(e.getKey(), (p = new Phi(e.getKey())).addFr(e.getValue()));
            p.fr.add(e.getValue());
        }
        if (indexCnt < 0) return;
        if (++indexCnt >= oprH.size()) {
            indexCnt = -1;
            blk.opr.indexOpr(this.mp);
        }
    }

    @Override
    public void indexPhi() {
        if (indexCnt < 0) return;
        if (++indexCnt >= oprH.size()) {
            for (Meta p : mp.values()) p.shrink();
            blk.opr.indexPhi();
        }
    }

    @Override
    public void indexMeta(Set<Meta> s) {

    }

    @Override
    public void index(Set<MVar> s) {
        if (++indexCnt < oprH.size()) return;
        indexCnt = 0;
        int sz = reqs.size();
        reqs.addAll(s);
        if (sz != reqs.size()) for (Index o : oprH) o.index(reqs);
    }

    public void index() {
        for (Index o : oprH) o.index(mp.keySet());
    }

    @Override
    public void collect() {
        for (Index o : oprH) o.collect();
    }

    @Override
    public String toString() {
        return "entry" + blk.id;
    }
}
