package engine;

import meta.Meta;
import meta.midt.MFunc;
import meta.midt.MVar;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface Index {
    void setFunc(MFunc f);
    void flushCnt();
    void indexOpr(Map<MVar, Meta> mp);
    void indexPhi();
    void indexMeta(Set<Meta> s);
    void index(Set<MVar> s);
    void collect();
}
