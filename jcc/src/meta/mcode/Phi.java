package meta.mcode;

import meta.Meta;
import meta.midt.MVar;

import java.util.ArrayList;
import java.util.List;

public class Phi extends Meta {
    public MVar var;
    public List<Meta> fr = new ArrayList<>();

    public Phi(MVar v) {
        super(false);
        this.var = v;
    }

    public Phi addFr(Meta m) {
        fr.add(m);
        return this;
    }

    @Override
    public int calc() {
        return var.gval();
    }

    @Override
    public boolean isCnst() {
        return cnst = false;
    }

    @Override
    public void shrink() {
        if (fr.isEmpty()) return;
        fr.removeIf(e -> e.eqls == this);
        boolean single = true;
        for (Meta m : fr)
            if (m.eqls != fr.get(0).eqls) {
                single = false;
                break;
            }
        if (single) this.eqls = fr.isEmpty() ? Nop : fr.get(0).eqls;
    }

    @Override
    public String toString() {
        return " Phi(" + var.name + ')';
    }

    @Override
    public Meta[] prevs() {
        return fr.toArray(new Meta[0]);
    }
}
