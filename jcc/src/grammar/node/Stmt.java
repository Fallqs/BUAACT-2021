package grammar.node;

import grammar.NTyp;
import grammar.Node;
import meta.Meta;

public class Stmt extends Node {
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

        return false;
    }

    @Override
    public Meta compile() {
        return null;
    }
}
