package meta.mcode;

import engine.instr.Instr;
import engine.instr.InstrDual;
import engine.instr.InstrLS;
import engine.instr.Op;
import meta.Meta;

public class Psi extends Meta implements Virtual {
    public Meta to;
    public Meta fr;

    public Psi(Meta fr, Meta to) {
        super(false);
        this.fr = fr;
        this.to = to;
    }

    @Override
    public boolean isCnst() {
        return fr.isCnst();
    }

    @Override
    public int calc() {
        return fr.calc();
    }

    @Override
    public String toString() {
        return "(T" + fr.id + " -> T" + to.id + ")";
    }

    @Override
    public Meta[] prevs() {
        return new Meta[]{fr};
    }

    private Instr loadVar(Meta tar, Put p) {
        int reg = tar.gtag(Instr.V0);
        Instr ret = new InstrLS(Op.lw, reg, p.var.base, Instr.bsR(p.var));
        if (reg == Instr.V0) ret = new InstrLS(Op.sw, reg, tar.spx, Instr.SP);
        return ret;
    }

    @Override
    public Instr translate() {
        to = to.eqls();
        fr = fr.eqls();
        if (fr instanceof Virtual) return null;
        if (fr instanceof Put) return loadVar(to, (Put) fr);
        if (to.reg >= 0 && fr.reg >= 0) return new InstrDual(Op.move, to.reg, fr.reg);
        if (to.reg >= 0) return new InstrLS(Op.lw, to.reg, fr.spx, Instr.SP);
        new InstrLS(Op.lw, Instr.V0, fr.spx, Instr.SP);
        return new InstrLS(Op.sw, Instr.V0, to.spx, Instr.SP);
    }
}
