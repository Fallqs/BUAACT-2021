package engine.instr;

import engine.Instr;

public class Nop implements Instr {
    public Nop() {
    }

    @Override
    public String toString() {
        return "nop";
    }
}
