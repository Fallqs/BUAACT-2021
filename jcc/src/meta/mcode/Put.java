package meta.mcode;

import engine.instr.Instr;
import engine.instr.InstrI;
import engine.instr.InstrLS;
import engine.instr.Op;
import meta.Meta;
import meta.midt.MTyp;
import meta.midt.MVar;

/**
 * put value to RAM / Global $, component of SyncO
 * Beware that FR maybe "function Call" or "Get operation",
 * for which this should do nothing.
 */
public class Put extends Meta {
    public final MVar var;

    public Put(MVar var) {
        super(false);
        this.var = var;
        valid = true;
    }

    @Override
    public boolean isCnst() {
        return cnst = var.cnst;
    }

    @Override
    public int calc() {
        return val = var.gval();
    }

    @Override
    public String toString() {
        return "(Global: " + var.name + ")";
    }

    @Override
    public void shrink() {
    }

    @Override
    public int get(int tmp) {
        new InstrLS(Op.lw, tmp, var.base, Instr.bsR(var));
        return tmp;
    }

    @Override
    public int get(int tmp, int shift) {
        if (var.typ != MTyp.Int) {
            if (Instr.bsR(var) == Instr.GP) new InstrI(Op.addi, tmp, Instr.GP, var.base);
            else new InstrI(Op.addi, tmp, Instr.SP, var.base + shift);
        } else {
            if (Instr.bsR(var) == Instr.GP) new InstrLS(Op.lw, tmp, var.base, Instr.bsR(var));
            else new InstrLS(Op.lw, tmp, var.base + shift, Instr.bsR(var));
        }
        return tmp;
    }

    @Override
    public Meta[] prevs() {
        return new Meta[0];
    }
}
