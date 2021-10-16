package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;
import word.Typ;

import java.util.ArrayList;

public class Block extends Node {
    private final ArrayList<Node> lines = new ArrayList<>();
    public boolean func = false;
    private int endp;

    public Block() {
        typ = NTyp.Block;
    }


    /* Block → '{' { BlockItem } '}' */
    @Override
    public boolean forward() {
        if (!cs.isTyp(Typ.LBRACE)) return false;
        cs.nex();
        Node ch;
        while ((ch = New.typ(NTyp.Decl)).fwd() || (ch = New.typ(NTyp.Stmt)).fwd()) lines.add(ch);
        cs.chkTil(Typ.RBRACE).nex();
        endp = cs.p;
        return true;
    }

    @Override
    public void logIdt() {
        for (Node i : lines) i.logIdt();
        if(func && idt.cur.retErr()) cs.chkErr(Typ.NULLRET, endp);
    }

    @Override
    public Meta compile() {
        return null;
    }
}
