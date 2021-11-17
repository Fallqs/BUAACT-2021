package meta.mcode;

import engine.sync.SyncR;
import meta.Meta;

/**
 * blez $cond, els
 * j then
 */
public class Brc extends Meta {
    public SyncR then, els;
    public Meta cond;

    public Brc(SyncR then) {
        super(false);
        this.then = then;
    }

    public Brc(Meta cond, SyncR then, SyncR els) {
        super(false);
        asLegend(this.cond = cond);
        this.then = then;
        this.els = els;
    }

    public Brc() {
    }

    @Override
    public String toString() {
        String ret = " Jmp " + then;
        if (cond != null) ret = " Branch " + els + "\n" + ret;
        return ret;
    }

    @Override
    public Meta[] prevs() {
        return new Meta[]{cond};
    }
}
