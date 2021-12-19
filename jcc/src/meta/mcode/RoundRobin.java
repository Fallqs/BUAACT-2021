package meta.mcode;

import engine.instr.Instr;
import engine.instr.InstrDual;
import engine.instr.Op;

import java.util.Arrays;
import java.util.List;

public class RoundRobin {
    public static int[] tar, fa;
    public static boolean[] vis;

    public static void dfs0(int x, int f) {
        if (tar[x] == -1) return;
        if (fa[tar[x]] != -1) {
            fa[tar[x]] = f;
            return;
        }
        fa[tar[x]] = f;
        dfs0(tar[x], f);
    }

    public static boolean dfs1(int x) {
        if (tar[x] == -1) return true;
        if (vis[tar[x]]) return false;
        vis[x] = true;
        boolean ans = dfs1(tar[x]);
        vis[x] = false;
        return ans;
    }

    public static void dfs2(int x, int fr) {
        vis[x] = true;
        if (tar[x] == -1) return;
        if (!vis[tar[x]]) dfs2(tar[x], tar[x]);
        new InstrDual(Op.move, tar[x], fr);
    }

    public static void sync(List<Integer> fr, List<Integer> to) {
        tar = new int[32];
        fa = new int[32];
        vis = new boolean[32];
        Arrays.fill(tar, -1);
        Arrays.fill(fa, -1);
        for (int i = 0; i<fr.size(); ++i) {
            if (!fr.get(i).equals(to.get(i))) tar[fr.get(i)] = to.get(i);
        }
        Arrays.fill(vis, false);
        for (int x = 0; x < 32; ++x) if (tar[x] != -1 && fa[x] == -1) {
            fa[x] = x;
            dfs0(x, x);
        }
        for (int x = 0; x < 32; ++x)
            if (fa[x] == x) {
                if (!dfs1(x)) {
                    new InstrDual(Op.move, Instr.A0, x);
                    dfs2(x, Instr.A0);
                } else dfs2(x, x);
            }
    }
}
