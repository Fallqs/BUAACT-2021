package meta;

import meta.ident.Env;
import meta.ident.Var;

import java.util.HashMap;
import java.util.Map;

public class Idents {
    private final Map<String, Env> env = new HashMap<>();
    public final Env sup = new Env();
    public Env cur = sup, fun;
    private Env ofun;

    public Idents() {
    }

    public Var query(String name) {
        Var v = cur.query(name);
        return v == null ? sup.query(name) : v;
    }

    public Env newEnv() {
        return cur = new Env();
    }

    public boolean merge() {
        if (cur.buf.name == null || env.containsKey(cur.buf.name) || sup.query(cur.buf.name) != null) return false;
        env.put(cur.buf.name, cur);
        sup.insert(cur.buf.name, new Var(0), false);
        return true;
    }

    public Env func(String name) {
        ofun = fun;
        return fun = env.get(name);
    }

    public Env ofun() {
        return fun = ofun;
    }
}
