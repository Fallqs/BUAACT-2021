package engine.instr;

public class InstrDual extends Instr {
    public int rt, rs;

    public InstrDual(Op op, int rt, int rs) {
        this.op = op;
        this.rt = rt;
        this.rs = rs;
    }

    @Override
    public String toString() {
        return op + " " + getReg(rt) + ", " + getReg(rs);
    }
}
