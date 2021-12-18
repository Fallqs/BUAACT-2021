package meta.midt;

import engine.Dojo;
import engine.MetaAlloc;
import engine.sync.SyncO;
import engine.sync.SyncR;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MFunc implements MIdt {
    public String name;
    public MTyp ret;
    public SyncR req;
    public MetaAlloc malloc;
    public final List<MVar> params = new ArrayList<>();
    public final Set<MVar> writes = new HashSet<>();
    public final Set<MVar> reads = new HashSet<>();
    public final List<MVar> defs = new ArrayList<>();
    public final Set<SyncO> oprs = new HashSet<>();
    public int stackSiz = 0;

    public MFunc(String name, MTyp ret, SyncR req) {
        this.name = name;
        this.ret = ret;
        this.req = req;
        Dojo.curFunc = this;
        malloc = new MetaAlloc();
    }

    public void write(MVar v) {
        if (v.global) writes.add(v);
    }

    public void write(SyncO o) {
        oprs.add(o);
    }

    @Override
    public MTyp typ() {
        return MTyp.Func;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return "MFunc{" +
                "name='" + name + '\'' +
                ", ret=" + ret +
                '}';
    }

    public void memAlloc() {
        malloc.distribute();
        stackSiz = malloc.stackSiz;
        for (MVar m : defs) {
            m.base = stackSiz;
            stackSiz += m.size << 2;
        }
        for (MVar p : params) {
            p.isParam = true;
            p.base = stackSiz;
            stackSiz += 4;
        }
        stackSiz += 4;
    }

    public boolean updRegUse(Set<Integer> s) {
        int siz = malloc.used.size();
        malloc.used.addAll(s);
        return malloc.used.size() == siz;
    }
}
