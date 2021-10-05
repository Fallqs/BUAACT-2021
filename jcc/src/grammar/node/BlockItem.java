package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;

public class BlockItem extends Node {
    public BlockItem() {
        typ = NTyp.BlockItem;
    }

    /* BlockItem → Decl | Stmt */
    @Override
    public boolean forward() {
        return true;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
