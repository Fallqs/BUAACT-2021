package meta.mcode;

import engine.instr.Instr;
import engine.instr.InstrJ;
import engine.instr.Op;
import meta.midt.MPin;

public class BrGoto extends Brc {
    public final boolean isBreak;
    public BrGoto(MPin pin, boolean isBreak) {
        pThen = pin;
        this.isBreak = isBreak;
    }

    @Override
    public Instr translate() {
        return translate(true);
    }

    @Override
    public Instr translate(boolean cond) {
        if (cond) sync(pThen.req);
        return new InstrJ(Op.j, pThen.req);
    }

    @Override
    public String toString() {
        return " GOTO " + pThen.req.toString();
    }
}
