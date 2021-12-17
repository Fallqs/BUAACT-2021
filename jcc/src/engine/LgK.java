package engine;

import meta.Meta;
import meta.Opr;

import java.util.Arrays;
import java.util.EnumSet;

import static meta.Opr.*;

public class LgK {
    /**
     * 生成DAG时使用的键值类
     */
    public final Meta[] ms;
    public final Opr opr;
    private static final EnumSet<Opr> single = EnumSet.of(not, cnst),
            symmetric = EnumSet.of(add, and, or, mult, eql);

    public LgK(Opr opr, Meta... ms) {
        for (int i = 0; i < ms.length; ++i) ms[i] = ms[i].eqls();
        this.ms = ms;
        this.opr = opr;
        if (symmetric.contains(opr)) Arrays.sort(this.ms);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LgK)) return false;
        LgK t = (LgK) o;
        if (t.ms.length != ms.length || t.opr != opr) return false;
        if (opr == cnst) {
            return ms[0].calc() == t.ms[0].calc();
        }
        for (int i = 0; i < ms.length; ++i) {
            ms[i] = ms[i].eqls();
            t.ms[i] = t.ms[i].eqls();
        }
        Arrays.sort(ms);
        Arrays.sort(t.ms);
        for (int i = 0; i < ms.length; ++i) if (ms[i].eqls() != t.ms[i].eqls()) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int ans = 0;
        for (Meta v : ms) ans = ans * 19260817 + v.id;
        return ans * 19260817 + opr.hashCode();
    }
}
