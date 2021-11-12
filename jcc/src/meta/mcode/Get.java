package meta.mcode;

import meta.Meta;
import meta.midt.MVar;

import java.util.ArrayList;
import java.util.List;

/**
 * get value from RAM / Global $, component of SyncR
 */
public class Get extends Meta {
    public MVar var;
    public List<Meta> putv;

    public Get(MVar var) {
        this.var = var;
        putv = new ArrayList<>(var.putv);
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
        return "(" + var.name + " -> T" + this.id + ")";
    }

    @Override
    public void collect() {
        if (ref == 0) for (Meta m : putv) m.collect();
        super.collect();
    }
}
