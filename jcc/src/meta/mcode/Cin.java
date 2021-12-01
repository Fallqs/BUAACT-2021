package meta.mcode;

import engine.instr.Instr;
import engine.instr.InstrI;
import engine.instr.InstrLS;
import engine.instr.InstrR;
import engine.instr.Nop;
import engine.instr.Op;
import meta.Meta;

public class Cin extends Meta {
    public Cin() {
        valid = true;
    }

    @Override
    public boolean isCnst() {
        return cnst = false;
    }

    @Override
    public String toString() {
        return " cin >> T" + this.id;
    }

    @Override
    public Instr translate() {
        new InstrI(Op.ori, Instr.V0, Instr.ZERO, 5);
        new Nop("syscall", true);
        Instr ret;
        setSave(Instr.V0);
        if (reg >= 0) ret = new InstrR(Op.or, reg, Instr.V0, Instr.ZERO);
        else ret = new InstrLS(Op.sw, Instr.V0, spx, Instr.SP);
        return ret;
    }
}
