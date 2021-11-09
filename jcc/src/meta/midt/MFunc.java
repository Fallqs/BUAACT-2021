package meta.midt;

import engine.SyncR;

public class MFunc implements MIdt {
    public String name;
    public MVar[] params;
    public MTyp ret;
    public SyncR req;

    public MFunc(String name, MTyp ret, MVar... params) {
        this.name = name;
        this.ret = ret;
        this.params = params;
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
