package meta.mcode;

import engine.Dojo;
import meta.Meta;
import meta.midt.MFunc;
import meta.midt.MVar;

import java.util.LinkedList;
import java.util.List;

public class Call extends Meta {
    public final MFunc func;
    public Meta[] params;
    public final List<Put> save = new LinkedList<>();

    public Call(MFunc f, Meta... params) {
        func = f;
        this.params = params;
        save.addAll(Dojo.curOpr.save());
        for (Put p : save) p.addLegend(this);
        Dojo.curOpr.flush(this);
        for (MVar v : f.writes.keySet()) Dojo.curOpr.upd(v, this);
        valid = true;
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
        StringBuilder str = new StringBuilder().append('(');
        for(Meta m : params)str.append(" push T").append(m.id).append('\n');
        return str.append(" Call ").append(func.name).append(')').toString();
    }

    @Override
    public Meta[] prevs() {
        return params;
    }
}
