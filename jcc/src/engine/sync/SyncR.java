package engine.sync;

import engine.Dojo;
import engine.Index;
import engine.instr.Instr;
import engine.instr.InstrLS;
import engine.instr.Nop;
import engine.instr.Op;
import meta.Meta;
import meta.mcode.Phi;
import meta.mcode.Ret;
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
    public final Set<Meta> llive = new HashSet<>();
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

    public boolean transOpr(Map<MVar, Meta> mp) {
        boolean ans = true;
        for (Map.Entry<MVar, Meta> e : mp.entrySet()) {
            Phi p = (Phi) this.mp.get(e.getKey());
            if (p == null) {
                this.mp.put(e.getKey(), (p = new Phi(e.getKey())));
                blk.opr.syncOpr(p);
                ans = false;
            } else if (!p.fr.contains(e.getValue())) {
                p.fr.add(e.getValue());
                ans = false;
            }
        }
        return ans;
    }

    public boolean transPhi() {
        boolean ans = true;
        for (Meta p : mp.values())
            ans &= ((Phi) p).shrank();
        return ans;
    }

    public boolean slimPhi() {
        boolean ans = true;
//        mp.values().removeIf(e -> e.eqls() != e);
        List<Meta> temp = new LinkedList<>(mp.values());
        temp.removeIf(e -> e != e.eqls());
        Meta[] table = temp.toArray(new Meta[0]);
        for (int i = 1; i < table.length; ++i) {
            for (int j = 0; j < i; ++j) if (((Phi) table[i]).chekEql((Phi) table[j])) {
                table[i].eqls = table[j].eqls();
                ans = false;
                break;
            }
        }
        for (Meta p : table)
            ans &= ((Phi) p).shrank();
        return ans;
    }

    public boolean transMeta(boolean concrete) {
        int siz = llive.size();
        Set<Meta> rlive = blk.opr.llive.keySet();
        for (Meta p : mp.values()) {
            if (!rlive.contains(p)) continue;
            p.valid = p == p.eqls();
//            blk.valid |= p.valid && concrete;
            rlive.remove(p);
        }
        llive.addAll(rlive);
        return llive.size() == siz;
    }

    public void validate(Set<Meta> s) {
        if (blk.valid) return;
        for (Meta p : mp.values()) {
            if (s.contains(p.eqls())) {
                blk.valid = true;
                break;
            }
        }
    }

    public void transLive() {
        Set<Meta> liv = new HashSet<>(mp.values());
        liv.removeIf(e -> !e.valid);
        for (Meta m : liv) {
            func.malloc.add(m);
            liv.forEach(n -> func.malloc.add(m, n));
            llive.forEach(n -> func.malloc.add(m, n));
        }
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
        if (func.req == this && !"main".equals(func.name) && !func.pure)
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
