package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;
import word.Result;
import word.Typ;

import java.util.ArrayList;

public class AddExp extends Node {
    private ArrayList<Node> mult = new ArrayList<>();
    private ArrayList<Result> opr = new ArrayList<>();

    public AddExp() {
        typ = NTyp.AddExp;
        autoDisplay = false;
    }

    /* AddExp → MulExp { ('+' | '−') MulExp } */
    @Override
    public boolean forward() {
        Node ch = New.typ(NTyp.MulExp);
        if (!ch.fwd()) return false;
        mult.add(ch);
        dump(typ);
        while (cs.isTyp(Typ.PLUS) || cs.isTyp(Typ.MINU)) {
            opr.add(cs.cont());
            cs.nex();
            if (!(ch = New.typ(NTyp.MulExp)).fwd()) break;
            mult.add(ch);
            dump(typ);
        }
        return true;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
