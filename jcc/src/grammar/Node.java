package grammar;

import meta.Cursor;
import meta.Err;
import meta.Meta;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {
    public static Cursor cs;
    public static List<Compo> output = new ArrayList<>();
    public boolean autoDisplay = true;
    public Meta meta;
    protected NTyp typ;

    public abstract boolean forward();

    public abstract Meta compile();

    public void dump(NTyp typ) {
        output.add(new Compo(typ, cs.pos()));
    }

    public void dump(NTyp typ, int p) {
        output.add(new Compo(typ, p));
    }

    public void dedump() {
        if (output.size() != 0) output.remove(output.size() - 1);
    }

    public NTyp gettyp() {
        return typ;
    }

    public boolean fwd() {
        boolean ret = forward();
        if (autoDisplay && ret && typ != null) dump(this.typ);
        return ret;
    }
}
