package meta.midt;

import engine.Dojo;
import engine.MetaAlloc;
import engine.sync.SyncO;
import engine.sync.SyncR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MFunc implements MIdt {
    public String name;
    public MTyp ret;
    public SyncR req;
    public MetaAlloc malloc;
    public final List<MVar> params = new ArrayList<>();
    public final Map<MVar, SyncO> writes = new HashMap<>();
    public final Set<SyncO> oprs = new HashSet<>();
    public int siz = 1;

    public MFunc(String name, MTyp ret, SyncR req) {
        this.name = name;
        this.ret = ret;
        this.req = req;
        Dojo.curFunc = this;
        malloc = new MetaAlloc();
    }

    public void write(SyncO o, MVar v) {
        writes.put(v, o);
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
}
