package engine.seg;

import engine.instr.*;
import meta.midt.MFunc;

import java.util.HashMap;
import java.util.Map;

public class Func extends Seg {

    private final Map<String, Integer> posp = new HashMap<>();
    private final Map<Integer, InstrBJ> posi = new HashMap<>();

    private String find(String tar) {
        Integer x = posp.get(tar);
        if (x == null) return tar;
        InstrBJ bj = posi.get(x);
        if (!(bj instanceof InstrJ)) return tar;
        return find(bj.getPin());
    }

    private void slim() {
        int ix = 0;
        Instr cur = begin;
        while (cur.nex != null) {
            if (cur instanceof Nop) {
                posp.put(((Nop) cur).toString(true), ix);
                cur = cur.nex;
                continue;
            }
            if (cur instanceof InstrBJ) posi.put(ix, (InstrBJ) cur);
            ++ix;
            cur = cur.nex;
        }

        for (Map.Entry<Integer, InstrBJ> e : posi.entrySet()) {
            InstrBJ bj = e.getValue();
            int pos = e.getKey();
            String tar = find(bj.getPin());
            bj.setPin(tar);
            Integer post = posp.get(tar);
            if (post != null && post == pos + 1) bj.reMov();
        }

    }

    public Func(MFunc f) {
        Instr.cur = null;
        begin = new Nop(f.name);
        f.req.translate();
        slim();
    }
}
