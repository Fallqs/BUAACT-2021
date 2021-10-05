package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;

public class EqExp extends Node {
    public EqExp() {
        typ = NTyp.EqExp;
    }

    /* EqExp → RelExp { ('==' | '!=') RelExp } */
    @Override
    public boolean forward() {

        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
