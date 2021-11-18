package engine.instr;

import engine.sync.SyncR;

public class InstrJ extends Instr {
    public String label;

    public InstrJ(Op op, String tar) {
        this.op = op;
        this.label = tar;
    }

    public InstrJ(Op op, SyncR tar) {
        this(op, tar.toString());
    }

    public InstrJ(Op op, Nop tar) {
        this(op, tar.toString());
    }

    @Override
    public String toString() {
        return op + " " + label;
    }
}
