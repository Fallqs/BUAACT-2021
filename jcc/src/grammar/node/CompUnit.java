package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;

import java.util.ArrayList;

public class CompUnit extends Node {
    private final ArrayList<Node> dcel = new ArrayList<>();
    private final ArrayList<Node> funcDef = new ArrayList<>();
    private Node mainFuncdef;

    public CompUnit() {}

    @Override
    public Boolean forward() {
        Node ch = New.typ(NTyp.Decl);
        while (ch.forward()) {
            dcel.add(ch);
            ch = New.typ(NTyp.Decl);
        }
        ch = New.typ(NTyp.FuncDef);
        while(ch.forward()) {
            funcDef.add(ch);
            ch = New.typ(NTyp.FuncDef);
        }
        mainFuncdef = New.typ(NTyp.MainFuncDef);
        return mainFuncdef.forward();
    }

    @Override
    public Meta compile() {
        return null;
    }
}
