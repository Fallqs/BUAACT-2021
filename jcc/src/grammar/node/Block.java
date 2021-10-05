package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;

public class Block extends Node {
    public Block () {
        typ = NTyp.Block;
    }

    /* Block → '{' { BlockItem } '}' */
    @Override
    public boolean forward() {

        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
