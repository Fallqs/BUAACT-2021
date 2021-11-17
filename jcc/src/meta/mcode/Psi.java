package meta.mcode;

import meta.Meta;

public class Psi extends Meta {
    public Meta to;
    public final Meta fr;

    public Psi(Meta fr, Meta to) {
        super(false);
        this.fr = fr;
        this.to = to;
    }

    @Override
    public boolean isCnst() {
        return fr.isCnst();
    }

    @Override
    public int calc() {
        return fr.calc();
    }

    @Override
    public String toString() {
        return "(T" + fr.id + " -> T" + to.id + ")";
    }

    @Override
    public Meta[] prevs() {
        return new Meta[]{fr};
    }
}
