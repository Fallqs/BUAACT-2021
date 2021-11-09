package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;
import word.Typ;

import java.util.ArrayList;

public class LAndExp extends Node {
    private ArrayList<Node> bl = new ArrayList<>();

    public LAndExp() {
        typ = NTyp.LAndExp;
        autoDisplay = false;
    }

    /* LAndExp → EqExp { '&&' EqExp } */
    @Override
    public boolean forward() {
        Node ch = New.typ(NTyp.EqExp);
        if (!ch.fwd()) return false;
        bl.add(ch);
        dump(typ);
        while (cs.isTyp(Typ.AND)) {
            cs.nex();
            if (!(ch = New.typ(NTyp.EqExp)).fwd()) break;
            bl.add(ch);
            dump(typ);
        }
        return true;
    }

    @Override
    public void logIdt() {
        for (Node i : bl) i.logIdt();
    }

    @Override
    public Meta translate() {
        return null;
    }
}
