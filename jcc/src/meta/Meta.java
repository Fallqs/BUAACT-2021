package meta;

import meta.mcode.LgK;

public class Meta {
    public final int id;
    public boolean cnst = false;
    public Meta eqls;
    protected static int cnt = 0;
    public int val = 0;

    public Meta() {
        id = ++cnt;
        eqls = this;
    }

    public static final Meta Nop = new Meta();

    public String toString() {
        return "(=> " + id + ")";
    }

    public LgK toLgk() {
        return new LgK(Opr.lw, id, id);
    }

    public static int log2(int x) {
        int i = 0;
        for (int t = 1; t < x; t <<= 1) ++i;
        return i;
    }

    public int calc() {
        return 0;
    }

    public boolean isCnst() {
        return false;
    }

    public void shrink() {
    }
}
