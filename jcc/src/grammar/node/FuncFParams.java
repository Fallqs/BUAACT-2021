package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;
import word.Typ;

import java.util.ArrayList;

public class FuncFParams extends Node {
    private ArrayList<Node> params = new ArrayList<>();

    public FuncFParams() {
        typ = NTyp.FuncFParams;
    }

    /* FuncFParams → FuncFParam { ',' FuncFParam } */
    @Override
    public boolean forward() {
        Node ch = New.typ(NTyp.FuncFParam);
        if (!ch.fwd()) return false;
        params.add(ch);
        while(cs.isTyp(Typ.COMMA)) {
            cs.nex();
            if(!(ch = New.typ(NTyp.FuncFParam)).fwd()) break;
            params.add(ch);
        }
        return true;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
