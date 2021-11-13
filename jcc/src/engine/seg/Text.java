package engine.seg;

import engine.instr.Instr;
import engine.instr.Nop;

public class Text extends Seg{
    public Text() {
        Instr.cur = null;
        begin = new Nop(".text", true);

    }
}
