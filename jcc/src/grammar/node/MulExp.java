package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;
import word.Result;
import word.Typ;

import java.util.ArrayList;

public class MulExp extends Node {
    private ArrayList<Node> uny = new ArrayList<>();
    private ArrayList<Result> opr = new ArrayList<>();

    public MulExp() {
        typ = NTyp.MulExp;
        autoDisplay = false;
    }

    /**
     * MulExp → UnaryExp { ([*%/]) UnaryExp }
     */
    @Override
    public boolean forward() {
        Node ch = New.typ(NTyp.UnaryExp);
        if (!ch.fwd()) return false;
        uny.add(ch);
        dump(typ);
        while (cs.isTyp(Typ.MULT) || cs.isTyp(Typ.DIV) || cs.isTyp(Typ.MOD)) {
            opr.add(cs.cont());
            cs.nex();
            if (!(ch = New.typ(NTyp.UnaryExp)).fwd()) break;
            uny.add(ch);
            dump(typ);
        }
        return true;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
