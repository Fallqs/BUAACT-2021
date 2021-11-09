package engine;

import meta.Opr;

public class LgK {
    /**
     * 生成DAG时使用的键值类
     */
    public final int[] ms;
    public final Opr opr;

    public LgK(Opr opr, int... ms) {
        this.ms = ms;
        this.opr = opr;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LgK)) return false;
        LgK t = (LgK) o;
        if (t.ms.length != ms.length || t.opr != opr) return false;
        for (int i = 0; i < ms.length; ++i) if (ms[i] != t.ms[i]) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int ans = 0;
        for(int v: ms) ans = ans * 19260817 + v;
        return ans * 19260817 + opr.hashCode();
    }
}
