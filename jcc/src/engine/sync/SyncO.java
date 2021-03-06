package engine.sync;

import engine.Dojo;
import engine.Index;
import meta.Meta;
import meta.mcode.*;
import meta.midt.MFunc;
import meta.midt.MVar;

import java.util.*;

/**
 * Synchronize Operation
 */
public class SyncO implements Index {
    protected final Map<MVar, Meta> mp = new TreeMap<>();
    public final Set<SyncR> legendH = new TreeSet<>();
    public final Set<SyncR> legendL = new TreeSet<>();
    public final Set<Meta> rlive = new HashSet<>();
    public final Map<Meta, Integer> llive = new HashMap<>();
    public final Map<SyncR, ArrayList<Psi>> psi = new TreeMap<>();
    public final SyncB blk;
    public final SyncR rq;
    public Meta end;
    public boolean valid = false;

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

    public void addLegend(SyncR req) {
        legendH.add(req);
    }

    public void addLegendL(SyncR req) {
        legendL.add(req);
    }

    public void upd(MVar v, Meta m) {
        if (end != null) return;
        mp.put(v, m);
        if (Dojo.curFunc != null) Dojo.curFunc.write(v);
    }

    public Meta qry(MVar v) {
        if (!mp.containsKey(v)) mp.put(v, rq.qry(v));
        return mp.get(v);
    }

    public Map<MVar, Meta> save() {
        return new TreeMap<>(mp);
    }

    public int indexCnt = 0;

    @Override
    public void setFunc(MFunc f) {
        if (func == null) {
            func = f;
            for (Index i : legendH) i.setFunc(f);
        }
    }

    public void syncOpr(Phi p) {
        this.mp.putIfAbsent(p.var, p);
    }

    public boolean transOpr() {
        boolean ans = true;
        for (SyncR req : legendH) ans &= req.transOpr(new HashMap<>(this.mp));
        for (SyncR req : legendL) ans &= req.transOpr(new HashMap<>(this.mp));
        return ans;
    }

    private void loadRlive(Set<SyncR> legend) {
        for (SyncR lgd : legend) {
            if (lgd instanceof GlobalR && "main".equals(func.name)) continue;
            rlive.addAll(lgd.llive);
            for (Meta p : lgd.mp.values())
                if (p.valid) {
                    MVar var = ((Phi) p).var;
                    if (mp.containsKey(var)) rlive.add(mp.get(var).eqls());
                }
        }
    }

    private void loadRPhi(Set<SyncR> legend) {
        Set<Meta> validate = new HashSet<>();
        for (SyncR lgd : legend) {
            if (lgd instanceof GlobalR && "main".equals(func.name)) continue;
            for (Meta p : lgd.mp.values())
                if (p.valid && ((Phi) p).fr.size() > 1) {
                    MVar var = ((Phi) p).var;
                    if (mp.containsKey(var)) validate.add(mp.get(var).eqls());
                }
        }
        rq.validate(validate);
    }

    public boolean transMeta() {
        int siz = llive.size();
        if (!(end instanceof Ret) || !((Ret) end).func.name.equals("main")) {
            loadRlive(legendH);
            loadRlive(legendL);
        }
        for (Meta m : end.prevs()) {
            m.eqls().valid = true;
            llive.putIfAbsent(m.eqls(), 0);
        }
        rlive.forEach(e -> llive.putIfAbsent(e, 0));
        for (int i = blk.ms.size() - 1; i >= 0; --i) {
            Meta m = blk.ms.get(i).eqls();
            if (!m.valid && !rlive.contains(m)) continue;
            m.valid = true;
            llive.remove(m);
            for (Meta n : m.prevs()) {
                n.eqls().valid = true;
                llive.putIfAbsent(n.eqls(), 0);
            }
        }
        if (llive.size() != siz) blk.req.transMeta(false);
        return llive.size() == siz;
    }

    public boolean transValid() {
        int siz = llive.size();
        if (!(end instanceof Ret) || !((Ret) end).func.name.equals("main")) {
            loadRlive(legendH);
            loadRlive(legendL);
            loadRPhi(legendH);
            loadRPhi(legendL);
        }
        end.concrete |= blk.fa.valid;
        if (end instanceof Concrete || end.concrete) for (Meta m : end.prevs()) {
            m.eqls().valid = m.eqls().concrete = true;
            llive.putIfAbsent(m.eqls(), 0);
            blk.fa.valid = true;
        }
        rlive.forEach(e -> llive.putIfAbsent(e, 0));
        for (int i = blk.ms.size() - 1; i >= 0; --i) {
            Meta m = blk.ms.get(i).eqls();
            if (!( m.concrete || m instanceof Concrete && ((Concrete) m).be()) &&
                    !rlive.contains(m) && !(blk.fa.valid && m instanceof Brp)) continue;
            m.valid = m.concrete = true;
            blk.fa.valid |= rlive.contains(m);
            llive.remove(m);
            for (Meta n : m.prevs()) {
                n.eqls().concrete = n.eqls().valid = true;
                llive.putIfAbsent(n.eqls(), 0);
            }
        }
        if (llive.size() != siz) blk.req.transMeta(true);
        return llive.size() == siz;
    }

    private void setPsi(Set<SyncR> legend) {
        for (SyncR lgd : legend) {
            psi.putIfAbsent(lgd, new ArrayList<>());
            for (Meta p : lgd.mp.values())
                if (p.valid && mp.containsKey(((Phi) p).var)) {
                    psi.get(lgd).add(new Psi(mp.get(((Phi) p).var).eqls(), p));
                }
        }
    }

    private void transCall(Call c) {
        for (Map.Entry<MVar, Meta> e : rq.mp.entrySet()) {
            if (e.getValue().eqls().valid && !(e.getValue().eqls() instanceof Put))
                c.sync.put(e.getKey(), e.getValue().eqls());
        }
        c.sync.entrySet().removeIf(e -> !e.getKey().global || e.getValue() instanceof Virtual);
        for (Map.Entry<MVar, Meta> e : c.sync.entrySet()) {
            if (llive.containsKey(e.getValue())) c.retrieve.put(e.getKey(), e.getValue());
        }
        for (Meta m : llive.keySet()) if (!c.retrieve.containsValue(m)) c.preserve.add(m);
    }

    public void transLive() {
        setPsi(legendH);
        setPsi(legendL);
        ((Flight) end).addPsi(psi);
        for (Meta m : rlive) llive.merge(m, 1, Integer::sum);
        Map<Meta, Integer> ref = new HashMap<>();
        for (int i = blk.ms.size() - 1; i >= 0; --i) {
            Meta m = blk.ms.get(i);
            if (!m.valid) continue;
            ref.put(m, llive.getOrDefault(m, 0));
            llive.remove(m);
            for (Meta n : m.prevs()) llive.merge(n.eqls(), 1, Integer::sum);
        }
        for (int i = 0; i < blk.ms.size(); ++i) {
            Meta m = blk.ms.get(i);
            if (!m.valid) continue;
            if (!(m instanceof Virtual)) func.malloc.add(m);
            for (Meta p : m.prevs()) llive.merge(p.eqls(), -1, Integer::sum);
            llive.entrySet().removeIf(e -> e.getValue() <= 0);
            if (m instanceof Call) transCall((Call) m);
            if (!(m instanceof Virtual)) for (Meta n : llive.keySet()) func.malloc.add(m, n.eqls());
            llive.put(m, ref.getOrDefault(m, 0));
        }
    }

    private boolean go () {
        if (blk.valid) return true;
        if (end instanceof Brc) ((Brc) end).fr = blk.fa;
        boolean go = false;
        for(SyncR lgd : legendH) {
            SyncB v = lgd.blk;
            if (v == null || blk.fa != v.fa) {
                go = true;
                break;
            }
        }
        return go;
    }

    @Override
    public void translate() {
        if (indexCnt < 0) return;
        indexCnt = -1;
        if (blk.valid)  for (Meta m : blk.ms) if (m.valid) m.translate();
//        if (end instanceof Brc) ((Brc) end).translate(blk.valid);
//        else end.translate();
        if (go()) end.translate();
        for (SyncR l : legendH) l.translate();
    }

    public void setEnd(Meta m) {
        if (end != null) return;
        end = m;
        for (Meta i : mp.values()) end.asLegend(i);
    }

    public void setEnd() {
        if (end == null) setEnd(new Ret());
    }

    @Override
    public int compareTo(Index o) {
        if (o instanceof GlobalO) return 1;
        if (o instanceof GlobalR) return -1;
        if (o instanceof SyncO) {
            SyncO q = (SyncO) o;
            return Integer.compare(blk.id, q.blk.id);
        } else {
            SyncR q = (SyncR) o;
            if (blk.id == q.blk.id) return -1;
            return Integer.compare(blk.id, q.blk.id);
        }
    }

//    public void printMP() {
//        System.out.println("MP::");
//        for (Map.Entry<MVar, Meta> e : mp.entrySet()) if (e.getKey().name.equals("a")) System.out.println(e.getValue());
//    }
}
