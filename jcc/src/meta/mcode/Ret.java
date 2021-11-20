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
        new InstrLS(Op.lw, Instr.RA, func.stackSiz - 4, Instr.SP);
        if (isVoid) return new Jr();
        Instr ret;
        vl = vl.eqls;
        if (vl.reg >= 0) ret = new InstrR(Op.or, Instr.V0, vl.reg, Instr.ZERO);
        else ret = new InstrLS(Op.lw, Instr.V0, vl.spx, Instr.SP);
        new InstrI(Op.addi, Instr.SP, Instr.SP, func.stackSiz);
        new Jr();
        return ret;
    }

    @Override
    public void addPsi(Map<SyncR, ArrayList<Psi>> psi) {
        this.psi = psi;
        if (psi != null && psi.containsKey(Dojo.globalReq) && !"main".equals(func.name)) {
            List<Psi> list = psi.get(Dojo.globalReq);
            for (Psi p : list)
                if (p.to instanceof Phi) func.writes.add(p.fr.eqls.save = ((Phi) p.to.eqls).var);
        }
    }
}
