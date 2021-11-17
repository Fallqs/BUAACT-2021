package lex.node;

import lex.NTyp;
import lex.New;
import lex.Node;
import lex.nd.Ref;
import meta.Meta;
import meta.ident.Env;
import meta.midt.MTable;
import word.Typ;

import java.util.ArrayList;

public class Block extends Node {
    private final ArrayList<Node> lines = new ArrayList<>();
    public boolean func = false;
    private int endp;
    private Env env;

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
        env = func ? idt.cur : idt.cur.chEnv();
        for (Node i : lines) i.logIdt();
        if (!func) idt.cur.faEnv();
        if (func && idt.cur.retErr() && !ret()) cs.chkErr(Typ.NULLRET, endp);
    }

    private boolean ret() {
        if (lines.isEmpty()) return false;
        Node stmt = lines.get(lines.size() - 1);
        if (!(stmt instanceof Stmt)) return false;
        if (((Stmt) stmt).opr instanceof Return) return true;
        // else if (((Stmt) stmt).opr instanceof Block) return ((Block) ((Stmt) stmt).opr).ret();
        return false;
    }

    @Override
    public Meta translate() {
        if (!(fa instanceof Ref)) MTable.newBlock();
        for (Node o : lines) {
            o.translate();
            if (o instanceof Return) break;
        }
        if (!(fa instanceof Ref)) MTable.popBlock();
        return null;
    }
}
