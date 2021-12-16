package meta.mcode;

import engine.instr.*;
import engine.sync.SyncR;
import meta.Meta;
import meta.midt.MPin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * blez $cond, els
 * j then
 */
public class Brc extends Meta implements Flight, Virtual {
    public SyncR then, els;
    public MPin pThen = new MPin(""), pEls = new MPin("");
    public Meta cond;
    public Map<SyncR, ArrayList<Psi>> psi;

    public Brc(SyncR then, SyncR els) {
        super(false);
        this.then = then;
        this.els = els;
    }

    public Brc(Meta cond, SyncR then, SyncR els) {
        super(false);
        asLegend(this.cond = cond);
        this.then = then;
        this.els = els;
    }

    public Brc() {
        super(false);
    }

    @Override
    public String toString() {
        String ret = " Jmp " + then;
        if (els != null) ret = " Branch " + els + "\n" + ret;
        return ret;
    }

    @Override
    public Meta[] prevs() {
        return cond == null ? new Meta[0] : new Meta[]{cond};
    }

    @Override
    public void addPsi(Map<SyncR, ArrayList<Psi>> psi) {
        this.psi = psi;
    }

    private int[] tar;
    private int[] fa;
    private boolean[] vis;

    private void dfs0(int x, int f) {
        if (tar[x] == -1) return;
        if (fa[tar[x]] != -1) {
            fa[tar[x]] = f;
            return;
        }
        fa[tar[x]] = f;
        dfs0(tar[x], f);
    }

    private boolean dfs1(int x) {
        if (tar[x] == -1) return true;
        if (vis[tar[x]]) return false;
        vis[x] = true;
        boolean ans = dfs1(tar[x]);
        vis[x] = false;
        return ans;
    }

    private void dfs2(int x, int fr) {
        vis[x] = true;
        if (tar[x] == -1) return;
        if (!vis[tar[x]]) dfs2(tar[x], tar[x]);
        new InstrDual(Op.move, tar[x], fr);
    }

    protected void sync(SyncR req) {
        if (psi != null && psi.containsKey(req)) {
            List<Psi> list = psi.get(req);
            list.removeIf(e -> !e.to.valid);
            tar = new int[32];
            fa = new int[32];
            vis = new boolean[32];
            Arrays.fill(tar, -1);
            Arrays.fill(fa, -1);
            for (Psi p : list) {
                p.to = p.to.eqls();
                p.fr = p.fr.eqls();
                if (p.fr == p.to) continue;
                if (p.to.reg < 0) p.translate();
                else if (p.fr.reg >= 0 && p.fr.reg != p.to.reg) tar[p.fr.reg] = p.to.reg;
            }
            Arrays.fill(vis, false);
            for (int x = 0; x < 32; ++x) if (tar[x] != -1 && fa[x] == -1) {
                fa[x] = x;
                dfs0(x, x);
            }
            for (int x = 0; x < 32; ++x)
                if (fa[x] == x) {
                    if (!dfs1(x)) {
                        new InstrDual(Op.move, Instr.A0, x);
                        dfs2(x, Instr.A0);
                    } else dfs2(x, x);
                }
            for (Psi p : list) if (p.fr.reg < 0) p.translate();
        }
    }

    @Override
    public Instr translate() {
        if (els == null) {
            sync(then);
            return new InstrJ(Op.j, then);
        }
//        InstrB cnd = new InstrB(Op.beq, cond.get(Instr.V0), Instr.ZERO, els);
        pThen.pin = new Nop().toString(true);
        sync(then);
        new InstrJ(Op.j, then);
//        cnd.label = new Nop().toString(true);
        pEls.pin = new Nop().toString(true);
        sync(els);
        new InstrJ(Op.j, els);
        return null;
    }
}
