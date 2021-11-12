package grammar.node;

import engine.Dojo;
import engine.SyncB;
import engine.SyncO;
import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Meta;
import meta.mcode.Brc;
import meta.midt.MTable;
import word.Typ;

public class While extends Node {
    private Node cond, stmt;

    public While() {
        typ = NTyp.While;
        autoDisplay = false;
    }

    /* 'while' '(' Cond ')' Stmt */
    @Override
    public boolean forward() {
        if (!cs.isTyp(Typ.WHILETK)) return false;
        cs.chkTil(Typ.LPARENT).nex();
        (cond = New.typ(NTyp.Cond)).fwd();
        cs.chkErr(Typ.RPARENT).nex();
        ++cs.whl;
        (stmt = New.typ(NTyp.Stmt)).fwd();
        --cs.whl;
        return true;
    }

    @Override
    public void logIdt() {
        cond.logIdt();
        stmt.logIdt();
    }

    @Override
    public Meta translate() {
        SyncO begin = Dojo.curOpr;
        Brc b0 = new Brc();
        begin.setEnd(b0);
        b0.then = new SyncB().req;

        Brc b1 = new Brc(cond.translate(), null, null);
        Dojo.curOpr.setEnd(b1);
        b1.then = new SyncB().req;

        stmt.translate();
        Brc b2 = new Brc();
        Dojo.curOpr.setEnd(b2);
        b1.els = b2.then = new SyncB().req;
        return null;
    }
}
