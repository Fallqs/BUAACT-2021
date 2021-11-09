package meta.mcode;

import meta.Meta;
import meta.midt.MVar;

/**
 * get value from RAM / Global $, component of SyncR
 */
public class Get extends Meta {
    public MVar var;

    public Get(MVar var) {
        this.var = var;
    }

    @Override
    public boolean isCnst() {
        return cnst = var.cnst;
    }

    @Override
    public int calc() {
        return var.gval();
    }

    @Override
    public String toString() {
        return "(" + var.name + " -> " + this.id + ")";
    }
}
