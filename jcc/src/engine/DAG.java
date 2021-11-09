package engine;

import meta.Meta;
import meta.mcode.LgK;

import java.util.HashMap;
import java.util.Map;

public class DAG {
    /**
     * 生成DAG时使用的容器类
     */
    private static final Map<LgK, Meta> s = new HashMap<>();

    public static void newDag() {
        s.clear();
    }

    public static Meta eqls(Meta m) {
        if(m == null) return Meta.Nop;
        s.putIfAbsent(m.toLgk(), m);
        return s.get(m.toLgk());
    }
}
