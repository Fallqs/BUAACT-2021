package meta.mcode;

import engine.instr.Instr;
import engine.instr.InstrJ;
import engine.instr.Op;
import meta.midt.MPin;

public class BrGoto extends Brc {
    public BrGoto(MPin pin) {
        pThen = pin;
    }

    @Override
    public Instr translate() {
        sync(pThen.req);
        return new InstrJ(Op.j, pThen.req);
    }
}
