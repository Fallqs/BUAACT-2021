package lex.node;

import lex.NTyp;
import lex.Node;
import lex.nd.Decl;
import meta.Meta;
import meta.midt.MTable;

import java.util.ArrayList;

public class CompUnit extends Node {
    private final ArrayList<Node> decl = new ArrayList<>();
    private Node mainFuncdef;

    public CompUnit() {
        typ = NTyp.CompUnit;
    }

    /* CompUnit → {Decl} {FuncDef} MainFuncDef */
    @Override
    public boolean forward() {
        Node ch = new Decl();
        while (ch.fwd() && ch.gettyp() != NTyp.MainFuncDef) {
            decl.add(ch);
            ch = new Decl();
        }
        mainFuncdef = ch;
        while (mainFuncdef.gettyp() != NTyp.MainFuncDef) {
            if (!(mainFuncdef = new Decl()).fwd()) cs.nex();
        }
        return true;
    }

    @Override
    public void logIdt() {
        for (Node i : decl) i.logIdt();
        mainFuncdef.logIdt();
    }

    @Override
    public Meta translate() {
        MTable.newBlock();
        for(Node o: decl) o.translate();
        mainFuncdef.translate();
        MTable.popBlock();
        return null;
    }
}
