package meta.mcode;

import engine.instr.*;
import meta.Meta;
import meta.midt.MStr;

public class Cout extends Meta implements Virtual {
    private boolean isStr = false;
    private Meta m;
    private MStr s;

    public Cout(Meta m) {
        asLegend(this.m = m);
        valid = true;
    }

    public Cout(MStr s) {
        this.s = s;
        isStr = cnst = true;
        valid = true;
    }

    @Override
    public boolean isCnst() {
        return cnst = isStr || m.isCnst();
    }

    @Override
    public int calc() {
        return val = isStr ? s.size : m.calc();
    }

    @Override
    public String toString() {
        return " cout << " + (isStr ? s.toString() : "T" + m.eqls().id);
    }

    @Override
    public Meta[] prevs() {
        return isStr ? new Meta[0] : new Meta[]{m};
    }

    @Override
    public Instr translate() {
        if (isStr) {
            new InstrI(Op.addi, Instr.A0, Instr.GP, s.base);
            new InstrI(Op.ori, Instr.V0, Instr.ZERO, 4);
            new Nop("syscall", true);
        } else {
            m = m.eqls();
            if (m.reg >= 0) new InstrR(Op.or, Instr.A0, Instr.ZERO, m.reg);
            else m.get(Instr.A0);
            new InstrI(Op.ori, Instr.V0, Instr.ZERO, 1);
            new Nop("syscall", true);
        }
        return null;
    }
}
