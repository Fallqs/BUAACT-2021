package meta.midt;

import engine.SyncR;

public class MFunc implements MIdt {
    public String name;
    public MVar[] params;
    public MVTyp ret;
    public SyncR req;

    public MFunc(String name, MVTyp ret, MVar... params) {
        this.name = name;
        this.ret = ret;
        this.params = params;
    }

    @Override
    public MVTyp typ() {
        return MVTyp.Func;
    }

    @Override
    public String name() {
        return name;
    }
}
