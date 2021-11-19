package engine.seg;

import engine.instr.Array;
import engine.instr.Asciiz;
import engine.instr.Instr;
import engine.instr.Nop;
import meta.midt.MStr;
import meta.midt.MTable;
import meta.midt.MVar;

public class Data extends Seg {
    public Data() {
        Instr.cur = null;
        begin = new Nop(".data 0x10008000", true);
        int base = 0;
        for (MVar v : MTable.global) {
            v.base = base;
            new Array(v.tag(), v.size, v.putc);
            base += v.size << 2;
        }
        for (MStr s : MTable.strcon) {
            s.base = base;
            new Asciiz(s.name(), s.cont);
            for (char c : s.cont.toCharArray()) if ('\\' == c) --base;
            base += s.cont.length() + 1;
        }
    }
}
