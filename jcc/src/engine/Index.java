package engine;

import meta.Meta;
import meta.midt.MFunc;
import meta.midt.MVar;

import java.util.Map;
import java.util.Set;

public interface Index extends Comparable<Index> {
    void setFunc(MFunc f);
    void flushCnt();
    void indexOpr(Map<MVar, Meta> mp, boolean isLight);
    void indexPhi();
    void indexPhi(boolean isLoop);
    void indexMeta(Set<Meta> s, boolean isLight, boolean kill);
    void translate();
}
