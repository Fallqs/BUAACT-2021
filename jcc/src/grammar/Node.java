package grammar;

import meta.Cursor;
import meta.Meta;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {
    public static Cursor cs;
    public static List<Compo> output = new ArrayList<>();
    public static boolean display = true;
    public Meta meta;
    protected NTyp typ;

    public abstract boolean forward();

    public abstract Meta compile();

    public void dump(NTyp typ) {
        output.add(new Compo(typ, cs.p));
    }

    public boolean fwd() {
        boolean ret = forward();
        if(display && ret && typ != null) dump(this.typ);
        return ret;
    }
}
