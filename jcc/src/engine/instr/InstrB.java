package engine.instr;

import engine.MetaAlloc;
import engine.sync.SyncR;
import meta.midt.MPin;

public class InstrB extends Instr implements InstrBJ {
    public int rs, rt;
    public String label;
    public MPin pin;

    public InstrB(Op op, int rs, int rt, String tar) {
        this.op = op;
        this.rs = rs;
        this.rt = rt;
        pin = new MPin(this.label = tar);
    }

    public InstrB(Op op, int rs, int rt, SyncR tar) {
        this(op, rs, rt, tar.toString());
    }

    public InstrB(Op op, int rs, int rt, Nop tar) {
        this(op, rs, rt, tar.toString(true));
    }

    public InstrB(Op op, int rs, int rt, MPin pin) {
        this.op = op;
        this.rs = rs;
        this.rt = rt;
        this.pin = pin;
    }

    @Override
    public String toString() {
        return op + " " + getReg(rs) + ", " + getReg(rt) + ", " + pin.pin;
    }

    @Override
    public String getPin() {
        return pin.pin;
    }

    @Override
    public void setPin(String tar) {
        pin.pin = tar;
    }
}
