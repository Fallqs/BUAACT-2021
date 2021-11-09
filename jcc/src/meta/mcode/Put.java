package meta.mcode;

import meta.Meta;
import meta.midt.MVar;

/**
 * put value to RAM / Global $, component of SyncO
 */
public class Put extends Meta {
    public Meta fr;
    public final MVar var;

    public Put(MVar var, Meta fr) {
        this.var = var;
        this.fr = fr;
    }

    public void upd(Meta m) {
        fr = m;
    }

    @Override
    public boolean isCnst() {
        return cnst = fr.cnst;
    }

    @Override
    public int calc() {
        return val = fr.calc();
    }

    @Override
    public String toString() {
        return "(" + fr.id + " -> " + var.name + ")";
    }

    @Override
    public void collect() {
        if(ref == 0) fr.collect();
        super.collect();
    }
}
