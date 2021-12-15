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

import java.util.*;

/**
 * Synchronize Requirement
 */
public class SyncR implements Index {
    public final Map<MVar, Meta> mp = new TreeMap<>();
    public final Set<Index> oprH = new TreeSet<>();
    public final Set<Index> oprL = new HashSet<>();
    public final SyncB blk;
    public boolean isLoop = false, endLoop = false;

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
        mp.putIfAbsent(v, new Phi(v));
        return mp.get(v);
    }

    public int indexCnt = 0;

    @Override
    public void setFunc(MFunc f) {
        if (func == null) {
            func = f;
            blk.opr.setFunc(f);
            for (MVar v : mp.keySet()) if (v.global) func.reads.add(v);
        }
    }

    @Override
    public void flushCnt() {
        this.blk.opr.flushCnt();
        indexCnt = loopCnt = 0;
    }

    @Override
    public void indexOpr(Map<MVar, Meta> mp, boolean isLight) {
        for (Map.Entry<MVar, Meta> e : mp.entrySet()) {
            Phi p = (Phi) this.mp.get(e.getKey());
            if (p == null) this.mp.put(e.getKey(), (p = new Phi(e.getKey())));
            p.fr.add(e.getValue());
        }
        if (indexCnt < 0 || isLight) return;
        if (++indexCnt >= oprH.size()) {
            indexCnt = -1;
            blk.opr.indexOpr(this.mp, false);
        }
    }

    public boolean transOpr(Map<MVar, Meta> mp) {
        boolean ans = true;
        for (Map.Entry<MVar, Meta> e : mp.entrySet()) {
            Phi p = (Phi) this.mp.get(e.getKey());
            if (p == null) {
                this.mp.put(e.getKey(), (p = new Phi(e.getKey())));
                blk.opr.syncOpr(p);
                ans = false;
            }
            p.fr.add(e.getValue());
        }
        return ans;
    }

    public boolean transPhi() {
        boolean ans = true;
        for (Meta p : mp.values()) ans &= ((Phi) p).shrank();
        return ans;
    }

    @Override
    public void indexPhi() {
        if (indexCnt < 0) return;
        if (++indexCnt >= oprH.size()) {
            indexCnt = -1;
            for (Meta p : mp.values())
                p.shrink();
            blk.opr.indexPhi();
            for (Meta p : mp.values())
                p.shrink();
            if (isLoop) blk.opr.indexPhi(true);
        }
    }

    private int loopCnt = 0;

    @Override
    public void indexPhi(boolean isLoop) {
        if (loopCnt < 0) return;
        if (!this.isLoop && !this.endLoop && ++loopCnt >= oprL.size()) {
            loopCnt = -1;
            blk.opr.indexPhi(true);
        }
    }

    @Override
    public void indexMeta(Set<Meta> s, boolean isLight, boolean kill) {
        if (indexCnt < 0) return;
        indexCnt = -1;
        List<Meta> list = new ArrayList<>();
        for (Meta p : mp.values()) {
            if (!s.contains(p)) continue;
            s.remove(p);
            p.valid = true;
            func.malloc.add(p);
            for (Meta q : list) func.malloc.add(p.eqls(), q);
            list.add(p.eqls());
        }
        for (Meta q : list) for (Meta r : s) func.malloc.add(q, r.eqls());
        for (Index i : oprH) i.indexMeta(new TreeSet<>(s), false, kill);
        for (Index i : oprL) i.indexMeta(new TreeSet<>(s), true, kill);
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
        if (func.req == this && !"main".equals(func.name))
            new InstrLS(Op.sw, Instr.RA, func.stackSiz - 4, Instr.SP);
        if (func.req == this) {
            for (Meta p : mp.values()) if (p.valid) p.translate();
        } else for (Meta p : mp.values()) if (p.valid) ((Phi) p).save();

        blk.opr.translate();
    }

    @Override
    public int compareTo(Index o) {
        if (o instanceof GlobalO) return 1;
        if (o instanceof GlobalR) return -1;
        if (o instanceof SyncO) {
            SyncO q = (SyncO) o;
            if (blk.id == q.blk.id) return 1;
            return Integer.compare(blk.id, q.blk.id);
        } else {
            SyncR q = (SyncR) o;
            return Integer.compare(blk.id, q.blk.id);
        }
    }
}
