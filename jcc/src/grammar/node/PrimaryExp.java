package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;

public class PrimaryExp extends Node {
    public PrimaryExp() {
        typ = NTyp.PrimaryExp;
    }

    /* PrimaryExp → '(' Exp ')' | LVal | Number */
    @Override
    public boolean forward() {

        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
