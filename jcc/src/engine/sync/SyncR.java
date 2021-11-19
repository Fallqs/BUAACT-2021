package engine.sync;

import engine.Dojo;
import engine.Index;
import engine.instr.Instr;
import engine.instr.InstrLS;
import engine.instr.Nop;
import engine.instr.Op;
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
    protected final Set<Index> oprH = new HashSet<>();
    //    private final Set<Index> oprL = new HashSet<>();
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
//        oprL.add(opr);
        opr.addLegendL(this);
    }

    public Meta qry(MVar v) {
        mp.putIfAbsent(v, new Phi(v));
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
            indexCnt = -1;
            for (Meta p : mp.values())
                p.shrink();
            blk.opr.indexPhi();
            mp.entrySet().removeIf(e -> e.getValue().eqls != e.getValue());
        }
    }

    @Override
    public void indexMeta(Set<Meta> s) {
        if (indexCnt < 0) return;
        indexCnt = -1;
        for (Meta p : mp.values()) {
            if (!s.contains(p)) continue;
            s.remove(p);
            p.valid = true;
            func.malloc.add(p);
            for (Meta q : p.prevs())
                if (!s.contains(q)) {
                    for (Meta r : s) func.malloc.add(p.eqls, r.eqls);
                }
        }
        for (Index i : oprH) i.indexMeta(new HashSet<>(s));
    }

    @Override
    public String toString() {
        return "entry" + blk.id;
    }

    @Override
    public void translate() {
        if (indexCnt < 0) return;
        if (++indexCnt < oprH.size()) return;
        indexCnt = -1;
        new Nop(toString());
        if (func.req == this && !"main".equals(func.name)) new InstrLS(Op.sw, Instr.RA, func.stackSiz - 4, Instr.SP);
        if (func.req == this) {
            for (Meta p : mp.values()) if (p.valid) p.translate();
        } else for (Meta p : mp.values()) if (p.valid) ((Phi) p).save();

        blk.opr.translate();
    }
}
