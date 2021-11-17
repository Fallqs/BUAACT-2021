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

public class If extends Node {
    private Node cond, then, els;

    public If() {
        typ = NTyp.If;
        autoDisplay = false;
    }

    /* 'if' '(' Cond ')' Stmt [ 'else' Stmt ] */
    @Override
    public boolean forward() {
        if (!cs.isTyp(Typ.IFTK)) return false;
        cs.chkTil(Typ.LPARENT).nex();
        (cond = New.typ(NTyp.Cond)).fwd();
        cs.chkErr(Typ.RPARENT).nex();
        (then = New.typ(NTyp.Stmt)).fwd();
        if (cs.isTyp(Typ.ELSETK)) {
            cs.nex();
            (els = New.typ(NTyp.Stmt)).fwd();
        }
        return true;
    }

    @Override
    public void logIdt() {
        cond.logIdt();
        then.logIdt();
        if (els != null) els.logIdt();
    }

    @Override
    public Meta translate() {
        Brc c1 = new Brc(cond.translate(), null, null);
        Dojo.curOpr.setEnd(c1);
        SyncO o1 = Dojo.curOpr;

        c1.then = new SyncB().req;
        then.translate();
        SyncO o2 = Dojo.curOpr;

        if (els == null) {
            Brc c2 = new Brc();
            o2.setEnd(c2);
            c2.then = c1.els = new SyncB().req;
            if(!(o2.end instanceof Ret)) c1.els.add(o2);
        } else {
            Brc c2 = new Brc();
            o2.setEnd(c2);
            c1.els = new SyncB().req;
            els.translate();
            SyncO o3 = Dojo.curOpr;
            Brc c3 = new Brc();

            SyncR r4 = new SyncB().req;
            o3.setEnd(c3);
            c2.then = c3.then = r4;
            r4.add(o2);
            r4.add(o3);
        }
        c1.then.add(o1);
        c1.els.add(o1);
        return null;
    }
}
