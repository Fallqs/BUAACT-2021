package meta;

import node.NTyp;

public class Meta {
    public final NTyp opr;
    public final int lval, rval;
    public final int tar;

    public Meta(NTyp opr, int lval, int rval, int tar) {
        this.opr = opr;
        this.lval = lval;
        this.rval = rval;
        this.tar = tar;
    }

    public String toString() {
        return "(" + opr + ", " + lval + ", " + rval + ", " + tar + ")";
    }
}
