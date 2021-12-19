package meta.mcode;

import engine.instr.Instr;
import engine.instr.InstrDual;
import engine.instr.Op;

import java.util.*;

public class RoundRobin {
    private static int[] tar, fa = new int[32], deg = new int[32];
    public static boolean[] vis = new boolean[32], in = new boolean[32];
    private static final boolean[] knot = new boolean[32];
    private static final List<Set<Integer>> G = new ArrayList<>();

    private static int cnt = 0;

    public static void init() {
        cnt = 0;
        G.clear();
        Arrays.fill(deg, 0);
        for (int i = 0; i < 32; ++i) G.add(new HashSet<>());
    }

    public static void add(int u, int v) {
        if (u == v) return;
        G.get(u).add(v);
        ++deg[v];
        ++cnt;
    }

    public static void dfsC(int u) {
        if (vis[u]) return;
        vis[u] = in[u] = true;
        for (Integer v : G.get(u)) {
            if (!vis[v]) dfsC(v);
            else if (in[v])
                fa[u] = v;
        }
        in[u] = false;
    }

    public static void dfs(int u) {
        vis[u] = true;
        for (Integer v : G.get(u)) {
            if (v != fa[u]) {
                dfs(v);
                new InstrDual(Op.move, v, u);
//                System.out.println(v + "<=" + u);
            } else {
                new InstrDual(Op.move, Instr.A0, u);
//                System.out.println("X<=" + u);
            }
        }
        if (knot[u]) {
            new InstrDual(Op.move, u, Instr.A0);
//            System.out.println(u + "<=X");
        }
    }

    public static void alloc() {
        Arrays.fill(fa, -1);
        Arrays.fill(vis, false);
        Arrays.fill(knot, false);
        for (int i = 0; i < 32; ++i) if (!vis[i]) dfsC(i);
        for (int i = 0; i < 32; ++i) if (fa[i] != -1) knot[fa[i]] = true;
        Arrays.fill(vis, false);
        for (int i = 0; i < 32; ++i) if (!vis[i] && (deg[i] == 0 || knot[i])) dfs(i);
//        System.out.println();
    }
}
