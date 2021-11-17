package lex.node;

import engine.Dojo;
import engine.sync.SyncB;
import engine.sync.SyncO;
import engine.sync.SyncR;
import lex.NTyp;
import lex.New;
import lex.Node;
import meta.Meta;
import meta.mcode.Brc;
import meta.mcode.Ret;
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
        SyncO o1 = Dojo.curOpr;
        Brc c1 = new Brc(cond.translate(), null, null);
        o1.setEnd(c1);
        new SyncB();
        SyncR r2 = Dojo.curReq;
        c1.then = r2;
        r2.add(o1);
        stmt.translate();
        Brc c2 = new Brc(cond.translate(), null, null);
        c2.then = r2;
        SyncO o2 = Dojo.curOpr;
        o2.setEnd(c2);
        if (!(o2.end instanceof Ret)) r2.addL(o2);
        new SyncB();
        SyncR r3 = Dojo.curReq;
        c1.els = c2.els = r3;
        r3.add(o1);
        r3.add(o2);
        return null;
    }
}
