package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;

public class FuncFParam extends Node {
    public FuncFParam() {
        typ = NTyp.FuncFParam;
    }

    /* FuncFParam → BType Ident ['[' ']' { '[' ConstExp ']' }] */
    @Override
    public boolean forward() {

        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
