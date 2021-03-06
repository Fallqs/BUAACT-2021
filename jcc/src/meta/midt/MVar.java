package meta.midt;

import engine.Dojo;
import meta.Meta;
import meta.mcode.PArr;
import meta.mcode.Put;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MVar implements MIdt, Comparable<MVar> {
    public MTyp typ;
    public String name;
    public int base = 0, size = 1, lgt = 0, dim = 1;
    public int reg = -1;
    public boolean cnst = false, init = false, isParam = false, global;
    public int[] putc;
    public List<PArr> putv = new ArrayList<>();
    public Put param = null;

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
        global = Dojo.curFunc == null || "main".equals(Dojo.curFunc.name) && typ != MTyp.Int;
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
        if (typ == MTyp.Arr && i.length > 0) ix = i[0];
        else if (typ == MTyp.Mat) {
            if (i.length > 0) ix = (i[0] << lgt);
            if (i.length > 1) ix += i[1];
        }
        return ix;
    }

    public int xi(int i) {
        return ix(i / dim, i % dim);
    }

    public int gval(int... i) {
        return init ? putc[ix(i) % putc.length] : 0;
    }

    public void initc(int... vals) {
        init = true;
        putc = new int[size];
        Arrays.fill(putc, 0);
        for (int i = 0; i < vals.length; ++i) putc[xi(i)] = vals[i];
    }

    @Override
    public String toString() {
        return "MVar{" +
                "name=" + name + ", " +
                "typ=" + typ +
                '}';
    }

    public String tag() {
        return name + id;
    }

    @Override
    public int compareTo(MVar o) {
        return Integer.compare(id, o.id);
    }
}
