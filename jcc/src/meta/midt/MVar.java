package meta.midt;

import java.util.Arrays;

public class MVar implements MIdt {
    public MVTyp typ;
    public String name;
    public int base = 0, size = 1, lgt = 0, dim = 1;
    public boolean cnst, init = false, sp = false;
    public int[] val;

    public MVar(boolean cnst, int... dims) {
        this.cnst = cnst;
        if (dims.length == 0) {
            typ = MVTyp.Int;
        } else if (dims.length == 1) {
            typ = MVTyp.Arr;
            size = dims[0];
        } else {
            typ = MVTyp.Mat;
            lgt = MVar.log2(dim = dims[1]);
            size = dims[0] * (1 << lgt);
        }
        val = new int[]{0};
    }

    public MVar(String name, boolean cnst, int... dims) {
        this(cnst, dims);
        this.name = name;
    }

    @Override
    public MVTyp typ() {
        return typ;
    }

    @Override
    public String name() {
        return name;
    }

    public void setSp() {
        sp = true;
    }

    public boolean onSp() {
        return sp && !cnst;
    }

    public static int log2(int x) {
        int i = 0;
        for (int t = 1; t < x; t <<= 1) ++i;
        return i;
    }

    public int ix(int... i) {
        int ix = 0;
        if (typ == MVTyp.Int && i.length > 0) ix = i[0];
        else if (typ == MVTyp.Mat) {
            if (i.length > 0) ix = (i[0] << lgt);
            if (i.length > 1) ix += i[1];
        }
        return ix;
    }

    public int gval(int... i) {
        return init ? val[ix(i)] : 0;
    }

    public void initv(int... vals) {
        init = true;
        val = new int[size];
        Arrays.fill(val, 0);
        for (int i = 0; i < vals.length; ++i) val[ix(i / dim, i % dim)] = vals[i];
    }
}
