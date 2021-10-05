package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;

public class UnaryExp extends Node {
    public UnaryExp() {
        typ = NTyp.UnaryExp;
    }

    /* UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp */
    @Override
    public boolean forward() {

        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
