package engine;

import meta.Meta;
import meta.midt.MFunc;
import meta.midt.MVar;

import java.util.Map;
import java.util.Set;

public interface Index extends Comparable<Index> {
    void setFunc(MFunc f);
    void translate();
}
