package meta.ident;

import word.Typ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Env {
    public final Map<String, Var> mp = new HashMap<>();
    private final List<Var> params = new ArrayList<>();
    private final List<String> paramName = new ArrayList<>();
    public Typ ret = Typ.VOIDTK;
    public final Buf buf = new Buf();
    private Var retV;

    public Env() {
    }

    public static class Buf {
        public String name;
        public final List<Integer> siz = new ArrayList<>();
        public int dim, ix;
        public boolean cnst = false, zero = false, onDecl = false, nonConst = false;
        public boolean paramCnt = false, paramErr = false;

        public Buf() {
        }
    }

    public Var buildVar() {
        Var var = new Var(buf.dim).setCnst(buf.cnst).setZero(buf.zero);
        if (buf.siz.size() != 0) for (int i = 0; i < buf.siz.size(); ++i) var.siz[i] = buf.siz.get(i);
        return var;
    }

    public boolean insert(String name, Var v, boolean isParam) {
        if (isParam) {
            params.add(v);
            paramName.add(name);
        }
        if (mp.containsKey(name)) return false;
        mp.put(name, v);
        return true;
    }

    public boolean addVar(boolean isParam) {
        if (buf.name == null) return false;
        return insert(buf.name, buildVar(), isParam);
    }

    public int paramSiz() {
        return params.size();
    }

    public boolean chkParam(int ix, Var v) {
        return ix >= params.size() || params.get(ix).chkParam(v);
    }

    public void setRet(Typ v) {
        ret = v;
    }

    public void setRetV(Var v) {
        retV = v;
    }

    public boolean retErr() {
        return ret == Typ.INTTK && retV == null;
    }

    public Var query(String name) {
        return mp.get(name);
    }
}
