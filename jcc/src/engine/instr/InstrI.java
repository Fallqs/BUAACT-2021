package engine.instr;

import engine.MetaAlloc;

public class InstrI extends Instr {
    public int rt, rs, imm;

    public InstrI(Op op, int rt, int rs, int imm) {
        this.op = op;
        this.rt = rt;
        this.rs = rs;
        this.imm = imm;
    }

    @Override
    public String toString() {
        return op + " " + getReg(rt) + ", " + getReg(rs) + ", " + imm;
    }
}
