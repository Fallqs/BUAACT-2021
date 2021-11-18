package meta.mcode;

import engine.Dojo;
import engine.instr.Instr;
import engine.instr.InstrLS;
import engine.instr.InstrSI;
import engine.instr.Op;
import meta.Meta;
import meta.midt.MVar;

import java.util.LinkedList;
import java.util.List;

public class PArr extends Meta {
    public Meta[] fr;
    public boolean[] ban;
    public final MVar var;

    public PArr(MVar var, Meta... fr) {
        this.var = var;
        this.fr = fr;
        ban = new boolean[fr.length];
        cnst = false;
        var.putv.add(this);
        Dojo.upd(var, this);
        for (Meta m : fr) m.addLegend(this);
    }

    @Override
    public boolean isCnst() {
        if (cnst) return true;
        for (Meta i : fr) if (!i.isCnst()) return false;
        return cnst = true;
    }

    public void toCnst() {
        if (!isCnst()) return;
        int[] val = new int[fr.length];
        for (int i = 0; i < fr.length; ++i) val[i] = fr[i].calc();
        var.initc(val);
    }

    @Override
    public int calc() {
        return 0;
    }

    @Override
    public void collect() {
        if (ref == 0) {
            for (int i = 0; i < fr.length; ++i) if (!ban[i]) fr[i].collect();
        }
        super.collect();
    }

    @Override
    public Meta[] prevs() {
        List<Meta> ret = new LinkedList<>();
        for (int i = 0; i < fr.length; ++i) if (!ban[i]) ret.add(fr[i]);
        return ret.toArray(new Meta[0]);
    }

    @Override
    public Instr translate() {
        for (int i = 0; i < fr.length; ++i) {
            if (fr[i].isCnst()) {
                new InstrSI(Op.li, Instr.V0, fr[i].calc());
                new InstrLS(Op.sw, Instr.V0, var.base + (i << 2), Instr.bsR(var));
            } else {
                new InstrLS(Op.sw, fr[i].get(Instr.V0), var.base + (i << 2), Instr.bsR(var));
            }
        }
        return null;
    }
}
