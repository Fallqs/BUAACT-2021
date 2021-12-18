package engine.sync;

import engine.Dojo;
import engine.Index;
import engine.instr.Instr;
import meta.Meta;
import meta.mcode.Concrete;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SyncB implements Comparable<SyncB> {
    public final SyncR req = new SyncR(this);
    public final SyncO opr = new SyncO(this);
    public final ArrayList<Meta> ms = new ArrayList<>();
    private static int cnt = 0;
    public final int id;
    public boolean valid = false, remove = false;
    public int vis = 0, dfn, low, sizV = 0, sizA = 0;
    public SyncB fa = this;
    public SyncB foreign = null;

    public SyncB() {
        Dojo.add(req);
        Dojo.add(opr);
        Dojo.add(this);
        id = ++cnt;
    }

    public void embed(Meta m) {
        ms.add(m);
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        if (req.func != null && req.func.req == req) ret.append('\n').append(req.func);
        ret.append('\n').append(req).append(":\n");
        if (valid) {
            for (Meta m : req.mp.values())
                if (m.valid) ret.append(m).append(": ")
                        .append(Instr.getReg(m.reg)).append('\n');
            for (Meta m : ms)
                if (m.valid) ret.append(m).append(": ").append(m.legend).append("; ")
                        .append(Instr.getReg(m.reg)).append('\n');
            ret.append(opr.end).append('\n');
        }
        return ret.toString();
    }

    @Override
    public int compareTo(SyncB o) {
        return Integer.compare(id, o.id);
    }

    public void checkValid() {
        for (Meta m : ms) {
            valid |= m instanceof Concrete && ((Concrete) m).be();
            if (valid) break;
        }
        valid |= opr.end instanceof Concrete && ((Concrete) opr.end).be();
    }

    public boolean checkForeign() {
        if (!fa.valid) return true;
        boolean sat = fa.foreign != fa;
        fa.foreign = fa;
        boolean ret = false;
        List<Index> prv = new ArrayList<>(req.oprH);
        prv.addAll(req.oprL);
        for (Index opr : prv) {
            SyncB u = ((SyncO) opr).blk;
            if (u == null || u.fa == fa) continue;
            ret |= u.fa.updForeign(fa.foreign);
        }
        return !ret || sat;
    }

    public boolean updForeign(SyncB blk) {
        if (foreign == null) {
            foreign = blk;
            return true;
        } else if (foreign != blk && foreign != this) {
            foreign = this;
            valid = true;
            return true;
        }
        return false;
    }
}
