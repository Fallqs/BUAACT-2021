package engine.sync;

import engine.Index;
import meta.Meta;
import meta.mcode.Phi;
import meta.midt.MFunc;
import meta.midt.MVar;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GlobalR extends SyncR {
    @Override
    public Meta qry(MVar v) {
        if (!mp.containsKey(v)) {
            Phi p = new Phi(v);
//            p.valid = true;
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
    public void indexOpr(Map<MVar, Meta> mp) {
    }

    @Override
    public void indexMeta(Set<Meta> s) {
        for (Index i : oprH) i.indexMeta(new HashSet<>());
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
}
