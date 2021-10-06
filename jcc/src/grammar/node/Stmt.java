package grammar.node;

import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;
import word.Typ;

public class Stmt extends Node {
    private Node opr;

    public Stmt() {
        typ = NTyp.Stmt;
    }

    /*
     * Stmt → LVal '=' Exp ';'
     * LVal = 'getint' '(' ')' ';'
     * 'printf' '(' FormatString {,Exp} ')' ';'
     * [Exp] ';'
     * Block
     * 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
     * 'while' '(' Cond ')' Stmt
     * break' ';'
     * 'continue' ';'
     * 'return' [Exp] ';'
     */
    @Override
    public boolean forward() {
        if(cs.isTyp(Typ.SEMICN)) {
            opr = null;
            return true;
        }
        if((opr = New.typ(NTyp.If)).fwd()) return true;
        if((opr = New.typ(NTyp.While)).fwd()) return true;
        if((opr = New.typ(NTyp.Break)).fwd()) return true;
        if((opr = New.typ(NTyp.Continue)).fwd()) return true;
        if((opr = New.typ(NTyp.Return)).fwd()) return true;
        if((opr = New.typ(NTyp.Printf)).fwd()) return true;
        if((opr = New.typ(NTyp.Block)).fwd()) return true;
        return (opr = New.typ(NTyp.StmtLR)).fwd();
    }

    @Override
    public Meta compile() {
        return null;
    }
}