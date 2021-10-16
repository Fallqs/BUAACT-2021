package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;
import word.Typ;

import java.util.ArrayList;

public class LOrExp extends Node {
    private ArrayList<Node> and = new ArrayList<>();

    public LOrExp() {
        typ = NTyp.LOrExp;
        autoDisplay = false;
    }

    /* LOrExp → LAndExp { '||' LAndExp } */
    @Override
    public boolean forward() {
        Node ch = New.typ(NTyp.LAndExp);
        if (!ch.fwd()) return false;
        and.add(ch);
        dump(typ);
        while (cs.isTyp(Typ.OR)) {
            cs.nex();
            if (!(ch = New.typ(NTyp.LAndExp)).fwd()) break;
            and.add(ch);
            dump(typ);
        }
        return true;
    }

    @Override
    public void logIdt() {
        for (Node i : and) i.logIdt();
    }

    @Override
    public Meta compile() {
        return null;
    }
}
