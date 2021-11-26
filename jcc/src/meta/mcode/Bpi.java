package meta.mcode;

import engine.instr.Instr;
import engine.instr.Nop;
import meta.Meta;
import meta.midt.MPin;

/**
 * to locate a MPin
 */
public class Bpi extends Meta implements Virtual {
    public MPin pin;

    public Bpi(MPin pin) {
        this.pin = pin;
        this.valid = true;
    }

    @Override
    public Instr translate() {
        Nop ret = new Nop();
        pin.pin = ret.toString(true);
        return ret;
    }
}
