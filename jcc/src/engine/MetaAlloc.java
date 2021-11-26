package engine;

import meta.Meta;
import meta.mcode.Put;

import java.util.*;

public class MetaAlloc {
    public static final String[] regs = {
            "a1", "a2", "a3", "t0", "t1", "t2", "t3", "t4", "t5", "t6", "t7",
            "s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7", "t8", "t9", "fp", "v1"
    };
    public final List<List<Integer>> G = new ArrayList<>();
    public final Map<Meta, Integer> mp = new HashMap<>();
    private int cnt = 0;
    public int[] regAlloc, stackAlloc, regUse;
    public final Set<Integer> used = new HashSet<>();
    public int stackSiz = 0;

    public MetaAlloc() {
        G.add(new ArrayList<>());
    }

    public void add(Meta u) {
        if (u != null && u != Meta.Nop && !mp.containsKey(u)) {
            mp.put(u, ++cnt);
            G.add(new ArrayList<>());
        }
    }

    public void add(Meta u, Meta v) {
        if (u == null || v == null || u == Meta.Nop || v == Meta.Nop
                || u instanceof Put || v instanceof Put) return;
        add(u);
        add(v);
        int iu = mp.get(u), iv = mp.get(v);
        G.get(iu).add(iv);
        G.get(iv).add(iu);
    }

    private int[] deduce() {
        int[] p = new int[cnt + 1], h = new int[cnt + 1];
        int[] nxt = new int[cnt + 1], lst = new int[cnt + 1];
        int[] deg = new int[cnt + 1];
        boolean[] tf = new boolean[cnt + 1];

        Arrays.fill(tf, false);
        Arrays.fill(deg, 0);
        Arrays.fill(h, 0);
        h[0] = 1;
        for (int i = 0; i <= cnt; ++i) {
            nxt[i] = i + 1;
            lst[i] = i - 1;
        }
        nxt[cnt] = 0;

        int cur = cnt, nww = 0;
        while (cur != 0) {
            p[cur] = h[nww];
            h[nww] = nxt[h[nww]];
            lst[h[nww]] = 0;
            lst[p[cur]] = nxt[p[cur]] = 0;
            tf[p[cur]] = true;
            for (int v : G.get(p[cur]))
                if (!tf[v]) {
                    if (h[deg[v]] == v) h[deg[v]] = nxt[v];
                    nxt[lst[v]] = nxt[v];
                    lst[nxt[v]] = lst[v];
                    lst[v] = nxt[v] = 0;
                    ++deg[v];
                    nxt[v] = h[deg[v]];
                    lst[h[deg[v]]] = v;
                    h[deg[v]] = v;
                }
            --cur;
            if (h[nww + 1] != 0) ++nww;
            while (nww > 0 && h[nww] == 0) --nww;
        }

        return p;
    }

    private int[] color(int... p) {
        int[] tag = new int[p.length + 1];
        int[] clr = new int[p.length];
        Arrays.fill(clr, 0);
        Arrays.fill(tag, 0);
        int cnt = 0;

        for (int i = p.length - 1, x; i > 0; --i) {
            x = p[i];
            for (int v : G.get(x)) tag[clr[v]] = x;
            int c = 1;
            while (tag[c] == x) ++c;
            clr[x] = c;
            cnt = Math.max(cnt, c);
        }
        clr[0] = cnt;
        return clr;
    }

    private void alloc() {
        int[] colors = color(deduce());
        int[] colorSum = new int[colors[0] + 1];
        stackSiz = colorSum.length << 2;

        for (int c : colors) ++colorSum[c];
        List<Integer> order = new ArrayList<>();
        for (int i = 1; i < colorSum.length; ++i) order.add(i);
        order.sort((x, y) -> Integer.compare(colorSum[y], colorSum[x]));
        for (int i = 0; i < order.size(); ++i) colorSum[order.get(i)] = i;
        regAlloc = new int[colors.length];
        stackAlloc = new int[colors.length];
        for (int i = 1; i < colors.length; ++i) {
            regAlloc[i] = colorSum[colors[i]];
            if (regAlloc[i] >= regs.length) regAlloc[i] = -1;
            stackAlloc[i] = colors[i] << 2;
        }
        order.clear();  // Random Shuffle
        for (int i = 0; i < regs.length; ++i) order.add(i);
        Collections.shuffle(order);
        regUse = new int[Math.min(regs.length, colors.length)];
        for (int i = 0; i < regUse.length; ++i) regUse[i] = order.get(i);
    }

    public void distribute() {
        alloc();
        for (Map.Entry<Meta, Integer> e : mp.entrySet()) {
            e.getKey().reg = regAlloc[e.getValue()];
            e.getKey().spx = stackAlloc[e.getValue()];
        }
        for (Meta m : mp.keySet()) if (m.reg != -1) m.reg = regUse[m.reg];
        for (int i : regUse) used.add(i);
    }
}
