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
    public final Set<Meta> alive = new TreeSet<>();
    public final Set<Meta> llive = new TreeSet<>();
    public final Map<SyncR, ArrayList<Psi>> psi = new TreeMap<>();
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
        return new TreeMap<>(mp);
    }

    public int indexCnt = 0, lightCnt = 0;

    @Override
    public void setFunc(MFunc f) {
        if (func == null) {
            func = f;
            for (Index i : legendH) i.setFunc(f);
        }
    }

    @Override
    public void flushCnt() {
        for (Index i : legendH) i.flushCnt();
        indexCnt = lightCnt = 0;
        if (legendL.isEmpty()) lightCnt = -1;
    }

    @Override
    public void indexOpr(Map<MVar, Meta> mp, boolean isLight) {
        if (indexCnt < 0) return;
        indexCnt = -1;
        for (Map.Entry<MVar, Meta> e : mp.entrySet())
            this.mp.putIfAbsent(e.getKey(), e.getValue());
        for (SyncR req : legendH) req.indexOpr(this.mp, false);
        for (SyncR req : legendL) req.indexOpr(this.mp, true);
    }

    @Override
    public void indexPhi() {
        if (indexCnt < 0) return;
        indexCnt = -1;
        if (!((end instanceof BrGoto) && ((BrGoto) end).isBreak)) for (Index i : legendH) i.indexPhi();
    }

    @Override
    public void indexPhi(boolean isBreak) {
        if (indexCnt < -1) return;
        indexCnt = -2;
        if ((end instanceof BrGoto) && ((BrGoto) end).isBreak) for (Index i : legendH) i.indexPhi();
        else for (Index i : legendH) i.indexPhi(true);
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
            if (!c.sync.containsKey(e.getKey()) &&
                    !(e.getValue().valid || e.getValue() instanceof Phi && e.getValue().eqls() == e.getValue()))
                c.sync.put(e.getKey(), e.getValue().eqls());
        }
        c.sync.entrySet().removeIf(e -> !e.getKey().global || e.getValue() instanceof Virtual);
        for (Map.Entry<MVar, Meta> e : c.sync.entrySet()) {
            if (alive.contains(e.getValue())) c.retrieve.put(e.getKey(), e.getValue());
        }
        for (Meta m : alive) if (!c.retrieve.containsValue(m)) c.preserve.add(m);
    }

    private final Stack<SyncLog> kills = new Stack<>();
    private final Stack<Meta> killsBuf = new Stack<>();

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
        while (!kills.isEmpty() && kills.peek().key == m) {
            if (!llive.contains(kills.peek().value)) alive.remove(kills.pop().value);
            else killsBuf.push(kills.pop().value);
        }
        if (!(m instanceof Virtual)) {
            for (Meta n : alive) func.malloc.add(m, n.eqls());
            alive.add(m);
        }
        while (!killsBuf.empty()) alive.remove(killsBuf.pop());
    }

    @Override
    public void indexMeta(Set<Meta> s, boolean isLight) {
        for (Meta m : s) alive.add(m.eqls());
        if (!isLight && indexCnt >= 0 && ++indexCnt >= legendH.size()) {
            indexCnt = -1;
            Set<MVar> vars = new TreeSet<>();
            if (!(end instanceof Ret) || !"main".equals(this.func.name)) phi(vars, legendH);
            phi(vars, legendL);
            ((Flight) end).addPsi(psi);
            for (Map.Entry<MVar, Meta> e : mp.entrySet())
                if (vars.contains(e.getKey())) e.getValue().eqls().valid = true;
            List<Meta> soup = blk.ms;
            end.valid = true;
            alive.removeIf(e -> e.eqls() instanceof Put);
            indexAlive(end);
            for (int i = soup.size() - 1; i > -1; --i) {
                indexAlive(soup.get(i));
            }
            blk.req.indexMeta(new TreeSet<>(alive), false);
            for (Meta m : blk.ms) if (m.valid) indexKill(m);
        }
        if (isLight && lightCnt >= 0) {
            if (++lightCnt >= legendL.size()) lightCnt = -1;
            llive.addAll(s);
        }
//        if (indexCnt < 0 && lightCnt < 0) {
//            for (Meta m : blk.ms) if (m.valid) indexKill(m);
//        }
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

    @Override
    public int compareTo(Index o) {
        if (o instanceof GlobalO) return 1;
        if (o instanceof GlobalR) return -1;
        if (o instanceof SyncO) {
            SyncO q = (SyncO) o;
            return Integer.compare(blk.id, q.blk.id);
        } else {
            SyncR q = (SyncR) o;
            return Integer.compare(blk.id, q.blk.id);
        }
    }
}
