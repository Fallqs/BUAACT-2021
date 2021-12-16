package engine.sync;

import engine.Index;
import meta.Meta;
import meta.mcode.Phi;
import meta.midt.MFunc;
import meta.midt.MVar;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class GlobalR extends SyncR {
    @Override
    public Meta qry(MVar v) {
        if (!mp.containsKey(v)) {
            Phi p = new Phi(v);
            p.valid = true;
            mp.put(v, p);
        }
        return mp.get(v);
    }

    @Override
    public void setFunc(MFunc f) {
    }

    @Override
    public boolean transOpr(Map<MVar, Meta> mp) {
        return true;
    }

    @Override
    public String toString() {
        return "Global_Req";
    }

    @Override
    public void translate() {
    }

    @Override
    public int compareTo(Index o) {
        return o == this ? 0 : 1;
    }
}
