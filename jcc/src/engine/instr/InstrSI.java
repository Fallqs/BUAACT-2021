package engine.instr;

public class InstrSI extends Instr {
    public int rt, imm;

    public InstrSI(Op op, int rt, int imm) {
        this.op = op;
        this.rt = rt;
        this.imm = imm;
    }

    @Override
    public String toString() {
        return op + " " + getReg(rt) + ", " + imm;
    }
}
