package engine.instr;

import engine.sync.SyncR;
import meta.midt.MPin;

public class InstrJ extends Instr implements InstrBJ {
    public String label;
    public MPin pin;

    public InstrJ(Op op, String tar) {
        this.op = op;
        pin = new MPin(this.label = tar);
    }

    public InstrJ(Op op, SyncR tar) {
        this(op, tar.toString());
    }

    public InstrJ(Op op, Nop tar) {
        this(op, tar.toString(true));
    }

    public InstrJ(Op op, MPin pin) {
        this.op = op;
        this.pin = pin;
    }

    @Override
    public String toString() {
        return op + " " + pin.pin;
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
