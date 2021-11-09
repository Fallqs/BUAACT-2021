package meta;

import engine.LgK;

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

    public int calc() {
        return 0;
    }

    public boolean isCnst() {
        return false;
    }

    public void shrink() {
    }

    public int ref = 0;
    public void collect() {
        ++ref;
    }
}
