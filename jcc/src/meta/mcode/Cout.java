package meta.mcode;

import meta.Meta;
import meta.midt.MStr;

public class Cout extends Meta {
    private boolean isStr = false;
    private Meta m;
    private MStr s;

    public Cout(Meta m) {
        asLegend(this.m = m);
        valid = true;
    }

    public Cout(MStr s) {
        this.s = s;
        isStr = cnst = true;
        valid = true;
    }

    @Override
    public boolean isCnst() {
        return cnst = isStr || m.isCnst();
    }

    @Override
    public int calc() {
        return isStr ? s.size : m.calc();
    }

    @Override
    public String toString() {
        return "Cout << " + (isStr ? s.toString() : m.id);
    }
}
