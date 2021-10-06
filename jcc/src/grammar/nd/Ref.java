package grammar.nd;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;
import word.Result;
import word.Typ;

import java.util.ArrayList;

public class Ref extends Node {
    private Result name;
    private ArrayList<Node> params = new ArrayList<>();
    private Node body;
    private final NTyp ind;
    private final NTyp para;

    public Ref(boolean display, boolean def) {
        this.typ = NTyp.NULL;
        this.autoDisplay = display;
        ind = def ? NTyp.ConstExp : NTyp.Exp;
        para = def ? NTyp.FuncFParams : NTyp.FuncRParams;
    }

    @Override
    public boolean forward() {
        if (!cs.isTyp(Typ.IDENFR) && !cs.isTyp(Typ.MAINTK)) return false;
        name = cs.cont();
        switch (cs.nex().typ()) {
            case LBRACK -> { //[
                this.typ = NTyp.LVal;
                Node ch = New.typ(ind);
                while (ch.fwd()) {
                    params.add(ch);
                    if (!cs.isTyp(Typ.RBRACK)) break;
                    if (!cs.nex().isTyp(Typ.LBRACK)) break;
                    cs.nex();
                    ch = New.typ(ind);
                }
            }
            case LPARENT -> { //(
                this.typ = name.typ == Typ.MAINTK ? NTyp.MainFuncDef : NTyp.FuncDef;
                Node ch = New.typ(para);
                if (ch.fwd()) params.add(ch);
                while (!cs.isTyp(Typ.RPARENT)) cs.nex();
                cs.nex();
                (body = New.typ(NTyp.Block)).fwd();
            }
            default -> this.typ = NTyp.LVal;
        }
        return true;
    }

    @Override
    public Meta compile() {
        return null;
    }

    public NTyp gettyp() {
        return typ;
    }
}
