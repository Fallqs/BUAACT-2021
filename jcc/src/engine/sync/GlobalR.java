package engine.sync;

import engine.Index;
import meta.Meta;
import meta.midt.MFunc;
import meta.midt.MVar;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GlobalR extends SyncR {
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
    public String toString() {
        return "Global_Seg";
    }

    @Override
    public void translate() {
    }
}
