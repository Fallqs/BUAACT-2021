package engine.instr;

import meta.midt.MPin;

public class InstrBZ extends Instr implements InstrBJ {
    public int rt;
    public MPin pin;

    public InstrBZ(Op op, int rt, MPin pin) {
        this.op = op;
        this.rt = rt;
        this.pin = pin;
    }

    @Override
    public String toString() {
        return op + " " + getReg(rt) + ", " + pin.pin;
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
