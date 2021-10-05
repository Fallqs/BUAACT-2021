package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;

public class RelExp extends Node {
    public RelExp() {
        typ = NTyp.RelExp;
    }

    /* RelExp → AddExp { ('<' | '>' | '<=' | '>=') AddExp } */
    @Override
    public boolean forward() {

        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
