package engine.seg;

import engine.instr.Instr;
import engine.instr.Nop;
import meta.midt.MFunc;

public class Func extends Seg {

    public Func(MFunc f) {
        Instr.cur = null;
        begin = new Nop(f.name);
        f.req.translate();
    }
}
