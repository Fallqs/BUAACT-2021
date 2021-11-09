package meta.mcode;

import meta.Meta;
import meta.midt.MVar;

/**
 * load value from HEAP
 */
public class GVal extends Meta {
    public int lgd = 0;
    public final MVar var;
    protected Meta[] ms;

    public GVal(MVar var, Meta... ms) {
        this.var = var;
        this.lgd = var.lgt;
        this.ms = ms;
    }

    @Override
    public boolean isCnst() {
        return cnst = var.cnst;
    }

    @Override
    public int calc() {
        int[] ix = new int[ms.length];
        for (int i = 0; i < ms.length; ++i) {
            ix[i] = ms[i].calc();
        }
        return var.gval(ix);
    }

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder();
        ans.append("(").append(var.name);
        for (Meta mi : ms) {
            ans.append("[").append(mi).append("]");
        }
        ans.append("@shift=").append(lgd).append(" -> ").append(this.id).append(")");
        return ans.toString();
    }
}
