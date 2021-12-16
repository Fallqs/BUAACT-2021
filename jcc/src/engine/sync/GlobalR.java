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
    public void flushCnt() {
    }

    @Override
    public void setFunc(MFunc f) {
    }

    @Override
    public void indexOpr(Map<MVar, Meta> mp, boolean isLight) {
    }

    @Override
    public boolean transOpr(Map<MVar, Meta> mp) {
        return true;
    }

    @Override
    public void indexMeta(Set<Meta> s, boolean isLight, boolean kill) {
        for (Index i : oprH)
            i.indexMeta(new TreeSet<>(), false, kill);
    }

    @Override
    public void indexPhi() {
    }

    @Override
    public void indexPhi(boolean isLoop) {
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
