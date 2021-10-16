package meta;

import meta.ident.Env;

import java.util.HashMap;
import java.util.Map;

public class Idents {
    private final Map<String, Env> env = new HashMap<>();
    public final Env sup = new Env();
    public Env cur = sup, fun;
    private Env ofun;

    public Idents() {
    }

    public Env newEnv() {
        return cur = new Env();
    }

    public boolean merge() {
        if (cur.buf.name == null || env.containsKey(cur.buf.name)) return false;
        env.put(cur.buf.name, cur);
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
