package engine.instr;

public class InstrS extends Instr {
    public int rd;

    public InstrS(Op op, int rd) {
        this.op = op;
        this.rd = rd;
    }

    @Override
    public String toString() {
        return op + " " + getReg(rd);
    }
}
