package engine;

import meta.midt.MVar;

import java.util.Set;

public interface Index {
    void index(Set<MVar> s);
    void collect();
}
