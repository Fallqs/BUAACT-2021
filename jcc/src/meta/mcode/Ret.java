package meta.mcode;

import engine.Dojo;
import engine.instr.Instr;
import engine.instr.InstrI;
import engine.instr.InstrLS;
import engine.instr.InstrR;
import engine.instr.Jr;
import engine.instr.Nop;
import engine.instr.Op;
import engine.sync.SyncR;
import meta.Meta;
import meta.midt.MFunc;
import meta.midt.MVar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Ret extends Meta implements Flight {
    private Meta vl;
    public boolean isVoid;
    public final MFunc func;
    public Map<SyncR, ArrayList<Psi>> psi;

    public Ret(Meta v) {
        super(false);
        asLegend(vl = v);
        isVoid = false;
        func = Dojo.curFunc;
    }

    public Ret() {
        super(false);
        isVoid = true;
        func = Dojo.curFunc;
    }

    @Override
    public int calc() {
        return val = isVoid ? 0 : vl.calc();
    }

    @Override
    public String toString() {
        return " Ret " + (isVoid ? "void" : "T" + vl.id);
    }

    @Override
    public Meta[] prevs() {
        return isVoid ? new Meta[0] : new Meta[]{vl};
    }

    @Override
    public Instr translate() {
        if ("main".equals(func.name)) {
            new InstrI(Op.ori, Instr.V0, Instr.ZERO, 10);
            new Nop("syscall", true);
            return null;
        }

        if (psi != null && psi.containsKey(Dojo.globalReq)) {
            List<Psi> list = psi.get(Dojo.globalReq);
            for (Psi p : list)
                if (p.to instanceof Phi) {
                    MVar v = ((Phi) p.to).var;
                    new InstrLS(Op.sw, p.fr.get(Instr.V0), v.base, Instr.bsR(v));
                }
        }

        new InstrLS(Op.lw, Instr.RA, func.stackSiz - 4, Instr.SP);
        if (isVoid) return null;
        Instr ret;
        if (vl.reg >= 0) ret = new InstrR(Op.or, Instr.V0, vl.reg, Instr.ZERO);
        else ret = new InstrLS(Op.lw, Instr.V0, vl.spx, Instr.SP);
        new Jr();
        return ret;
    }

    @Override
    public void addPsi(Map<SyncR, ArrayList<Psi>> psi) {
        this.psi = psi;
    }
}
