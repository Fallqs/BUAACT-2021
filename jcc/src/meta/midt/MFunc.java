package meta.midt;

import engine.Dojo;
import engine.SyncO;
import engine.SyncR;
import meta.Meta;

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
    public final List<MVar> params = new ArrayList<>();
    public final Map<MVar, SyncO> writes = new HashMap<>();
    public final Set<SyncO> oprs = new HashSet<>();

    public MFunc(String name, MTyp ret, SyncR req) {
        this.name = name;
        this.ret = ret;
        this.req = req;
        Dojo.curFunc = this;
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
}
