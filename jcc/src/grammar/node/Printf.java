package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;
import word.Result;
import word.Typ;

import java.util.ArrayList;

public class Printf extends Node {
    private Result fmt;
    private ArrayList<Node> exp = new ArrayList<>();

    public Printf() {
        autoDisplay = false;
    }

    /* 'printf' '(' FormatString { ',' Exp } ')' ';' */
    @Override
    public boolean forward() {
        if (!cs.isTyp(Typ.PRINTFTK)) return false;
        if (!cs.nex().isTyp(Typ.LPARENT))return true;
        if (!cs.nex().isTyp(Typ.STRCON)) return true;
        fmt = cs.cont();
        cs.nex();
        Node ch;
        while (cs.isTyp(Typ.COMMA)) {
            cs.nex();
            if (!(ch = New.typ(NTyp.Exp)).fwd()) break;
            exp.add(ch);
        }
        while(!cs.isTyp(Typ.RPARENT))cs.nex();
        while(!cs.isTyp(Typ.SEMICN))cs.nex();
        cs.nex();
        return true;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
