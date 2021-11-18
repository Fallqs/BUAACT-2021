package engine.seg;

import engine.instr.Instr;
import engine.instr.Nop;
import meta.midt.MFunc;
import meta.midt.MTable;

import java.util.LinkedList;
import java.util.List;

public class Text extends Seg {
    public final List<Func> list = new LinkedList<>();

    public Text() {
        Instr.cur = null;
        begin = new Nop(".text", true);
        for (MFunc f : MTable.func) list.add(new Func(f));
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(begin).append('\n');
        for (Func f : list) ret.append('\n').append(f).append('\n');
        return ret.toString();
    }
}
