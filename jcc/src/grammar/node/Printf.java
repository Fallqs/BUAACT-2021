package grammar.node;

import grammar.Node;
import meta.Meta;

public class Printf extends Node {
    public Printf() {}

    /* 'printf' '(' FormatString { ',' Exp } ')' ';' */
    @Override
    public boolean forward() {

        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
