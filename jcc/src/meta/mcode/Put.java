package meta.mcode;

import meta.Meta;
import meta.midt.MVar;

/**
 * put value to RAM / Global $, component of SyncO
 * Beware that FR maybe "function Call" or "Get operation",
 * for which this should do nothing.
 */
public class Put extends Meta {
    public Meta fr;
    public final MVar var;

    public Put(MVar var, Meta fr) {
        this.var = var;
        asLegend(this.fr = fr);
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
        return "(T" + fr.id + " -> " + var.name + ")";
    }

    @Override
    public void collect() {
        if(ref == 0) fr.collect();
        super.collect();
    }

    @Override
    public void shrink() {
        fr = fr.eqls;
    }

    @Override
    public Meta[] prevs() {
        return new Meta[]{fr};
    }
}
