package meta.mcode;

import engine.instr.Instr;
import engine.instr.InstrB;
import engine.instr.InstrJ;
import engine.instr.Op;
import meta.Meta;
import meta.midt.MPin;

public class Brp extends Meta implements Virtual {
    public MPin pThen, pEls;
    public Meta cond;

    public Brp(Meta cond, MPin then, MPin els) {
        this.cond = cond;
        pThen = then;
        pEls = els;
        this.valid = true;
    }

    @Override
    public Meta[] prevs() {
        return cond == null ? new Meta[0] : new Meta[]{cond};
    }

    @Override
    public Instr translate() {
        if (cond != null) {
            cond = cond.eqls();
            if (pEls != null) new InstrB(Op.beq, cond.get(Instr.V0), Instr.ZERO, pEls);
            if (pThen != null) new InstrB(Op.bne, cond.get(Instr.V0), Instr.ZERO, pThen);
        } else {
            new InstrJ(Op.j, pThen);
        }
        return null;
    }

    @Override
    public String toString() {
        String ret = "(";
        if (cond != null) {
            if (pEls != null) ret = ret + "beqz T" + cond.eqls().id + ", " + pEls.toString() + "\n";
            if (pThen != null) ret = ret + "bnez T" + cond.eqls().id + ", " + pThen.toString() + "\n";
            return ret + " )";
        }
        return " Jmp " + pThen.toString();
    }
}
