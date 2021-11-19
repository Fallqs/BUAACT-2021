package meta.mcode;

import engine.instr.Instr;
import engine.instr.InstrB;
import engine.instr.InstrJ;
import engine.instr.InstrLS;
import engine.instr.Nop;
import engine.instr.Op;
import engine.sync.SyncR;
import meta.Meta;
import meta.midt.MVar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * blez $cond, els
 * j then
 */
public class Brc extends Meta implements Flight {
    public SyncR then, els;
    public Meta cond;
    public Map<SyncR, ArrayList<Psi>> psi;

    public Brc(SyncR then) {
        super(false);
        this.then = then;
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
        if (cond != null) ret = " Branch " + els + "\n" + ret;
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

    private void sync(SyncR req) {
        if (psi != null && psi.containsKey(req)) {
            List<Psi> list = psi.get(req);
            for (Psi p : list) p.translate();
        }
    }

    @Override
    public Instr translate() {
        if (els == null) {
            sync(then);
            return new InstrJ(Op.j, then);
        }
        InstrB cnd = new InstrB(Op.beq, cond.get(Instr.V0), Instr.ZERO, els);
        sync(then);
        new InstrJ(Op.j, then);
        cnd.label = new Nop().toString(true);
        sync(els);
        new InstrJ(Op.j, els);
        return null;
    }
}
