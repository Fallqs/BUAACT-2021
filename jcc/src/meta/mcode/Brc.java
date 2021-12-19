package meta.mcode;

import engine.instr.*;
import engine.sync.SyncB;
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
    public SyncB fr = null;
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

    protected void sync(SyncR req) {
        if (psi != null && psi.containsKey(req)) {
            List<Psi> list = psi.get(req);
            list.removeIf(e -> !e.to.valid);
            RoundRobin.tar = new int[32];
            RoundRobin.fa = new int[32];
            RoundRobin.vis = new boolean[32];
            Arrays.fill(RoundRobin.tar, -1);
            Arrays.fill(RoundRobin.fa, -1);
            for (Psi p : list) {
                p.to = p.to.eqls();
                p.fr = p.fr.eqls();
                if (p.fr == p.to) continue;
                if (p.to.reg < 0) p.translate();
                else if (p.fr.reg >= 0 && p.fr.reg != p.to.reg) RoundRobin.tar[p.fr.reg] = p.to.reg;
            }
            Arrays.fill(RoundRobin.vis, false);
            for (int x = 0; x < 32; ++x) if (RoundRobin.tar[x] != -1 && RoundRobin.fa[x] == -1) {
                RoundRobin.fa[x] = x;
                RoundRobin.dfs0(x, x);
            }
            for (int x = 0; x < 32; ++x)
                if (RoundRobin.fa[x] == x) {
                    if (!RoundRobin.dfs1(x)) {
                        new InstrDual(Op.move, Instr.A0, x);
                        RoundRobin.dfs2(x, Instr.A0);
                    } else RoundRobin.dfs2(x, x);
                }
            for (Psi p : list) if (p.fr.reg < 0) p.translate();
        }
    }

    @Override
    public Instr translate() {
        return translate(fr == null);
    }

    public Instr translate(boolean cond) {
        if (els == null) {
            if (cond || then.blk.fa != fr) {
                sync(then);
                return new InstrJ(Op.j, then);
            } else return null;
        }
        pThen.pin = new Nop().toString(true);
        if (cond || then.blk.fa != fr) {
            sync(then);
            new InstrJ(Op.j, then);
        }
        pEls.pin = new Nop().toString(true);
        if (cond || els.blk.fa != fr) {
            sync(els);
            new InstrJ(Op.j, els);
        }
        return null;
    }
}
