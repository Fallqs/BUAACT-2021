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
    protected final Map<MVar, Meta> mp = new HashMap<>();
    public final Set<SyncR> legendH = new HashSet<>();
    public final Set<SyncR> legendL = new HashSet<>();
    public final Set<Meta> alive = new HashSet<>();
    public final Map<SyncR, ArrayList<Psi>> psi = new HashMap<>();
    public final SyncB blk;
    public final SyncR rq;
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
        return new HashMap<>(mp);
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
        if (indexCnt < 0) return;
        indexCnt = -1;
        for (Map.Entry<MVar, Meta> e : mp.entrySet())
            this.mp.putIfAbsent(e.getKey(), e.getValue());
        for (SyncR req : legendH) req.indexOpr(this.mp);
        for (SyncR req : legendL) req.indexOpr(this.mp);
    }

    @Override
    public void indexPhi() {
        if (indexCnt < 0) return;
        indexCnt = -1;
        for (Index i : legendH) i.indexPhi();
    }

    private void phi(Set<MVar> vars, Set<SyncR> lgd) {  // deduce (psi) nodes for every branch
        for (SyncR req : lgd) {
            psi.put(req, new ArrayList<>());
            for (Map.Entry<MVar, Meta> e : req.mp.entrySet()) {
                if (!(e.getValue() instanceof Phi) || e.getValue().eqls() != e.getValue()) continue;
                if (mp.containsKey(e.getKey())) {
                    vars.add(e.getKey());
                    Meta m = mp.get(e.getKey()).eqls();
                    psi.get(req).add(new Psi(m.eqls(), e.getValue().eqls()));
                    if (!(m instanceof Put)) alive.add(m);
                }
            }
        }
    }

    private void call(Call c) {
        for (Map.Entry<MVar, Meta> e : blk.req.mp.entrySet()) {
            if (!c.sync.containsKey(e.getKey())) c.sync.put(e.getKey(), e.getValue().eqls());
        }
        c.sync.entrySet().removeIf(e -> !e.getKey().global ||
                !(e.getValue().valid || e.getValue() instanceof Phi && e.getValue().eqls() == e.getValue()));
        for (Map.Entry<MVar, Meta> e : c.sync.entrySet()) {
            if (alive.contains(e.getValue())) c.retrieve.put(e.getKey(), e.getValue());
        }
        for (Meta m : alive) if (!c.retrieve.containsValue(m)) c.preserve.add(m);
    }

    private final Stack<SyncLog> kills = new Stack<>();

    private void indexAlive(Meta m) {
        if (!(m instanceof Virtual)) func.malloc.add(m);
        if (!alive.contains(m) && !m.valid && !(m instanceof Call)) return;
        alive.remove(m);
        m.valid = true;
        if (m instanceof Call) call((Call) m);
        for (Meta p : m.prevs())
            if (!alive.contains(p.eqls()) && !(p.eqls() instanceof Put)) {
//                for (Meta q : alive) func.malloc.add(p.eqls, q);
                kills.add(new SyncLog(m, p.eqls()));
                alive.add(p.eqls());
            }
    }

    private void indexKill(Meta m) {
        while (!kills.isEmpty() && kills.peek().key == m) alive.remove(kills.pop().value);
        if (!(m instanceof Virtual)) {
            for (Meta n : alive) func.malloc.add(m, n.eqls());
            alive.add(m);
        }
    }

    @Override
    public void indexMeta(Set<Meta> s) {
        for (Meta m : s) alive.add(m.eqls());
        if (indexCnt < 0) return;
        if (++indexCnt >= legendH.size()) {
            indexCnt = -1;
            Set<MVar> vars = new HashSet<>();
            phi(vars, legendH);
            phi(vars, legendL);
            ((Flight) end).addPsi(psi);
            for (Map.Entry<MVar, Meta> e : mp.entrySet()) if (vars.contains(e.getKey())) e.getValue().eqls().valid = true;
            List<Meta> soup = blk.ms;
            end.valid = true;
            alive.removeIf(e -> e.eqls() instanceof Put);
            indexAlive(end);
            for (int i = soup.size() - 1; i > -1; --i) {
                indexAlive(soup.get(i));
            }
            blk.req.indexMeta(new HashSet<>(alive));
            for (Meta m : soup) if (m.valid) indexKill(m);
        }
    }

    @Override
    public void translate() {
        if (indexCnt < 0) return;
        indexCnt = -1;
        for (Meta m : blk.ms) if (m.valid) m.translate();
        end.translate();
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
}
