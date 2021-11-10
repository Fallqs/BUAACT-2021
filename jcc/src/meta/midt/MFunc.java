package meta.midt;

import engine.Dojo;
import engine.SyncO;
import engine.SyncR;

import java.util.ArrayList;
import java.util.List;

public class MFunc implements MIdt {
    public String name;
    public MTyp ret;
    public SyncR req;
    public int index = 0;
    public final List<MVar> params = new ArrayList<>();
    public final List<MVar> writes = new ArrayList<>();

    public MFunc(String name, MTyp ret, SyncR req) {
        this.name = name;
        this.ret = ret;
        this.req = req;
        this.index = Dojo.code.size();
        Dojo.curFunc = this;
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
