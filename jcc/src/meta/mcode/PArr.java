package meta.mcode;

import engine.Dojo;
import meta.Meta;
import meta.midt.MVar;

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
        for(Meta m : fr) m.addLegend(this);
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
}
