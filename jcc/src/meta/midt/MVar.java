package meta.midt;

import engine.Dojo;
import meta.Meta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MVar implements MIdt {
    public MTyp typ;
    public String name;
    public int base = 0, size = 1, lgt = 0, dim = 1;
    public int reg = 0;
    public boolean cnst = false, init = false, param = false, global;
    public int[] putc;
    public List<Meta> putv = new ArrayList<>();

    private static int cnt = 0;
    private final int id;

    public MVar(int... dims) {
        id = ++cnt;
        if (dims.length == 0) {
            typ = MTyp.Int;
        } else if (dims.length == 1) {
            typ = MTyp.Arr;
            size = dims[0];
        } else {
            typ = MTyp.Mat;
            lgt = MVar.log2(dim = dims[1]);
            size = dims[0] * (1 << lgt);
        }
        putc = new int[]{0};
        global = Dojo.curFunc == null || "main".equals(Dojo.curFunc.name);
    }

    public MVar(String name, int... dims) {
        this(dims);
        this.name = name;
    }

    @Override
    public MTyp typ() {
        return typ;
    }

    @Override
    public String name() {
        return name;
    }

    public static int log2(int x) {
        int i = 0;
        for (int t = 1; t < x; t <<= 1) ++i;
        return i;
    }

    public int ix(int... i) {
        int ix = 0;
        if (typ == MTyp.Int && i.length > 0) ix = i[0];
        else if (typ == MTyp.Mat) {
            if (i.length > 0) ix = (i[0] << lgt);
            if (i.length > 1) ix += i[1];
        }
        return ix;
    }

    public int gval(int... i) {
        return init ? putc[ix(i) % putc.length] : 0;
    }

    public void initc(int... vals) {
        init = true;
        putc = new int[size];
        Arrays.fill(putc, 0);
        for (int i = 0; i < vals.length; ++i) putc[ix(i / dim, i % dim)] = vals[i];
    }

    @Override
    public String toString() {
        return "MVar{" +
                "name=" + name + ", " +
                "typ=" + typ +
                '}';
    }

    public String tag() {
        return "var" + id;
    }
}
