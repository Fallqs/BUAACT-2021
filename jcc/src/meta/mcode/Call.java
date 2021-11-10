package meta.mcode;

import engine.Dojo;
import meta.Meta;
import meta.midt.MFunc;
import meta.midt.MVar;

public class Call extends Meta {
    public final MFunc func;
    public Meta[] params;

    public Call(MFunc f, Meta... params) {
        func = f;
        this.params = params;
        for (MVar v : f.writes) Dojo.curOpr.upd(v, this);
    }

    @Override
    public boolean isCnst() {
        return cnst = false;
    }

    @Override
    public int calc() {
        return 0;
    }

    @Override
    public String toString() {
        return "Call " + func.name;
    }
}
