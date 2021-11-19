package meta.mcode;

import engine.instr.Instr;
import engine.instr.InstrDual;
import engine.instr.InstrLS;
import engine.instr.Op;
import meta.Meta;

public class Psi extends Meta {
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

    @Override
    public Instr translate() {
        to = to.eqls;
        fr = fr.eqls;
        if (to.reg >= 0 && fr.reg >= 0) return new InstrDual(Op.move, to.reg, fr.reg);
        if (to.reg >= 0) return new InstrLS(Op.lw, to.reg, fr.spx, Instr.SP);
        new InstrLS(Op.lw, Instr.V0, fr.spx, Instr.SP);
        return new InstrLS(Op.sw, Instr.V0, to.spx, Instr.SP);
    }
}
