package engine.seg;

import engine.instr.Instr;

public class Seg {
    public Instr begin;

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        Instr cur = begin;
        while (cur != null) {
            ret.append(cur).append('\n');
            cur = cur.nex;
        }
        return ret.toString();
    }
}
