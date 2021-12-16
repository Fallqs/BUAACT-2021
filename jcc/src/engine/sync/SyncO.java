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
    public final Set<Meta> lLive = new TreeSet<>(), rlive = new HashSet<>();
    public final Map<Meta, Integer> llive = new HashMap<>();
    public final Map<SyncR, ArrayList<Psi>> psi = new TreeMap<>();
    public final Set<Meta> loaded = new HashSet<>();
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

    public int indexCnt = 0, lightCnt = 0, breakCnt = 0;

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
        indexCnt = lightCnt = breakCnt = 0;
        if (legendL.isEmpty()) lightCnt = -1;
    }

    @Override
    public void indexOpr(Map<MVar, Meta> mp, boolean isLight) {
        for (Map.Entry<MVar, Meta> e : mp.entrySet())
            this.mp.putIfAbsent(e.getKey(), e.getValue());
        if (isLight || indexCnt < 0) return;
        indexCnt = -1;
        for (SyncR req : legendH) req.indexOpr(this.mp, false);
        for (SyncR req : legendL) req.indexOpr(this.mp, true);
    }

    public void syncOpr(Phi p) {
        this.mp.putIfAbsent(p.var, p);
    }

    public boolean transOpr() {
        boolean ans = true;
        for (SyncR req : legendH) ans &= req.transOpr(this.mp);
        for (SyncR req : legendL) ans &= req.transOpr(this.mp);
        return ans;
    }

    private void loadRlive(Set<SyncR> legend) {
        for (SyncR lgd : legend) {
            rlive.addAll(lgd.llive);
            for (Meta p : lgd.mp.values())
                if (p.valid) {
                    MVar var = ((Phi) p).var;
                    if (mp.containsKey(var)) rlive.add(mp.get(var).eqls());
                }
        }
    }

    public boolean transMeta() {
        int siz = llive.size();
        if (!(end instanceof Ret) || !((Ret) end).func.name.equals("main")) {
            loadRlive(legendH);
            loadRlive(legendL);
        }
        rlive.forEach(e -> {
            if (!loaded.contains(e)) llive.merge(e, 1, Integer::sum);
            loaded.add(e);
        });
        for (int i = blk.ms.size() - 1; i >= 0; --i) {
            Meta m = blk.ms.get(i).eqls();
            if (!m.valid && !rlive.contains(m) || loaded.contains(m)) continue;
            m.valid = true;
            llive.remove(m);
            loaded.add(m);
            for (Meta n : m.prevs()) llive.merge(n.eqls(), 1, Integer::sum);
        }
        if (llive.size() != siz) blk.req.transMeta();
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
        for (Map.Entry<MVar, Meta> e : blk.req.mp.entrySet()) {
            if (e.getValue().valid) c.sync.put(e.getKey(), e.getValue().eqls());
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
        for (int i = 0; i < blk.ms.size(); ++i) {
            Meta m = blk.ms.get(i);
            if (!m.valid) continue;
            for (Meta p : m.prevs()) llive.merge(p.eqls(), -1, Integer::sum);
            llive.entrySet().removeIf(e -> e.getValue() <= 0);
            if (m instanceof Call) transCall((Call) m);
            if (!(m instanceof Virtual)) for (Meta n : llive.keySet()) func.malloc.add(m, n.eqls());
        }
    }

    @Override
    public void indexPhi() {
        if (indexCnt < 0) return;
        indexCnt = -1;
        if (!((end instanceof BrGoto) && ((BrGoto) end).isBreak)) for (Index i : legendH) i.indexPhi();
    }

    @Override
    public void indexPhi(boolean isBreak) {
        if (breakCnt < 0) return;
        breakCnt = -1;
        if ((end instanceof BrGoto) && ((BrGoto) end).isBreak) for (Index i : legendH) i.indexPhi();
        else for (Index i : legendH) i.indexPhi(true);
    }

    private void phi(Set<MVar> vars, Set<SyncR> lgd, boolean kill) {  // deduce (psi) nodes for every branch
        for (SyncR req : lgd) {
            psi.putIfAbsent(req, new ArrayList<>());
            for (Map.Entry<MVar, Meta> e : req.mp.entrySet()) {
                if (!(e.getValue() instanceof Phi) || e.getValue().eqls() != e.getValue()) continue;
                if (mp.containsKey(e.getKey())) {
                    vars.add(e.getKey());
                    Meta m = mp.get(e.getKey()).eqls();
                    if (kill) psi.get(req).add(new Psi(m.eqls(), e.getValue().eqls()));
                    if (!(m instanceof Put)) alive.add(m);
                }
            }
        }
    }

    private void call(Call c) {
        for (Map.Entry<MVar, Meta> e : blk.req.mp.entrySet()) {
            if (!c.sync.containsKey(e.getKey()) && !(e.getValue().valid
                    || e.getValue() instanceof Phi && e.getValue().eqls() == e.getValue()))
                c.sync.put(e.getKey(), e.getValue().eqls());
        }
        c.sync.entrySet().removeIf(e -> !e.getKey().global || e.getValue() instanceof Virtual);
        for (Map.Entry<MVar, Meta> e : c.sync.entrySet()) {
            if (alive.contains(e.getValue())) c.retrieve.put(e.getKey(), e.getValue());
        }
        for (Meta m : alive) if (!c.retrieve.containsValue(m)) c.preserve.add(m);
    }

    private final Stack<SyncLog> kills = new Stack<>();

    private void indexAlive(Meta m, boolean kill) {
        if (!alive.contains(m) && !m.valid && !(m instanceof Call)) return;
        alive.remove(m);
        m.valid = true;
        if (m instanceof Call) call((Call) m);
        for (Meta p : m.prevs())
            if (!alive.contains(p.eqls()) && !(p.eqls() instanceof Put)) {
                if (kill) kills.add(new SyncLog(m, p.eqls()));
                alive.add(p.eqls());
            }
    }

    private void indexKill(Meta m) {
        func.malloc.add(m);
        while (!kills.isEmpty() && kills.peek().key == m) {
            if (!lLive.contains(kills.peek().value)) alive.remove(kills.peek().value);
            kills.pop();
        }
        if (!(m instanceof Virtual)) {
            for (Meta n : alive) func.malloc.add(m, n.eqls());
            alive.add(m);
        }
    }

    @Override
    public void indexMeta(Set<Meta> s, boolean isLight, boolean kill) {
        for (Meta m : s) {
            alive.add(m.eqls());
            lLive.add(m.eqls());
        }
        if (!isLight && indexCnt >= 0 && ++indexCnt >= legendH.size()) {
            indexCnt = -1;
            Set<MVar> vars = new TreeSet<>();
            if (!(end instanceof Ret) || !"main".equals(this.func.name)) phi(vars, legendH, kill);
            phi(vars, legendL, kill);
            if (kill) ((Flight) end).addPsi(psi);
            for (Map.Entry<MVar, Meta> e : mp.entrySet())
                if (vars.contains(e.getKey())) e.getValue().eqls().valid = true;
            List<Meta> soup = blk.ms;
            end.valid = true;
            alive.removeIf(e -> e.eqls() instanceof Put);
            indexAlive(end, kill);
            for (int i = soup.size() - 1; i > -1; --i) {
                indexAlive(soup.get(i), kill);
            }
            blk.req.indexMeta(new TreeSet<>(alive), false, kill);
            if (kill) {
                for (Meta m : blk.ms) if (m.valid) indexKill(m);
            }
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
}
