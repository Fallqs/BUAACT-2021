package meta.mcode;

import engine.Dojo;
import meta.Meta;
import meta.midt.MFunc;

public class Ret extends Meta {
    private Meta vl;
    public boolean isVoid;
    public final MFunc func;

    public Ret(Meta v) {
        asLegend(vl = v);
        isVoid = false;
        (func = Dojo.curFunc).write(Dojo.curOpr);
    }

    public Ret() {
        isVoid = true;
        func = Dojo.curFunc;
    }

    @Override
    public int calc() {
        return val = isVoid ? 0 : vl.calc();
    }

    @Override
    public String toString() {
        return " Ret " + (isVoid ? "void" : "T" + vl.id);
    }
}
