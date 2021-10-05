package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;

public class AddExp extends Node {
    public AddExp() {
        typ = NTyp.AddExp;
    }

    /* AddExp → MulExp { ('+' | '−') MulExp } */
    @Override
    public boolean forward() {

        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
