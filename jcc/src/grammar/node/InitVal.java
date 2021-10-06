package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;
import word.Typ;

import java.util.ArrayList;

public class InitVal extends Node {
    private Node exp;
    private ArrayList<Node> init = new ArrayList<>();

    public InitVal() {
        typ = NTyp.InitVal;
    }

    /**
     * InitVal → Exp | '{' [ InitVal { ',' InitVal } ] '}'
     */
    @Override
    public boolean forward() {
        if ((exp = New.typ(NTyp.Exp)).fwd()) return true;
        else if (cs.isTyp(Typ.LBRACE)) {
            exp = null;
            Node ch = New.typ(NTyp.InitVal);
            while (ch.fwd()) {
                init.add(ch);
                if (!cs.isTyp(Typ.COMMA)) break;
                cs.nex();
                ch = New.typ(NTyp.InitVal);
            }
            while(!cs.isTyp(Typ.RBRACE))cs.nex();
            cs.nex();
            return true;
        }
        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
