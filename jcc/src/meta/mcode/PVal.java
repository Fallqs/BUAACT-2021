package meta.mcode;

import meta.Meta;
import meta.midt.MVar;

import java.util.Arrays;

public class PVal extends Meta {
    public int lgd = 0;
    public Meta fr;
    public MVar var;
    protected Meta[] ms;

    public PVal(Meta fr, MVar var, Meta... ms) {
        this.fr = fr;
        this.var = var;
        this.ms = ms;
        lgd = var.lgt;
    }

    @Override
    public boolean isCnst() {
        return cnst = fr.isCnst();
    }

    @Override
    public int calc() {
        return val = fr.calc();
    }

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder();
        ans.append("(").append(fr.id).append("->").append(var.name);
        for (Meta mi : ms) {
            ans.append("[").append(mi).append("]");
        }
        ans.append(", shift=").append(lgd).append(")");
        return ans.toString();
    }

    @Override
    public void collect() {
        if (ref == 0) fr.collect();
        super.collect();
    }
}
