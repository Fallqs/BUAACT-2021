package engine;

import meta.Meta;
import meta.mcode.Calc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAG {
    /**
     * 生成DAG时使用的容器类
     */
    private final Map<LgK, Meta> s = new HashMap<>();
    private final ArrayList<Meta> ms;

    public DAG(ArrayList<Meta> ms) {
        this.ms = new ArrayList<>(ms);
        this.ms.removeIf(e -> !(e instanceof Calc));
        shrink();
    }

    public void shrink() {
        for (Meta m : ms) {
            Calc c = (Calc) m;
            LgK key = c.toLgk();
            Meta x = s.get(key);
            if (x != null) c.eqls = x;
            else s.put(key, c);
        }
    }
}
