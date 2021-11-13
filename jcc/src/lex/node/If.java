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
        Brc branch = new Brc();
        Dojo.curOpr.setEnd(branch);
        (branch.cond = cond.translate()).addLegend(branch);
        SyncO begin = Dojo.curOpr;

        branch.then = new SyncB().req;
        then.translate();
        SyncO opr = Dojo.curOpr;

        if (els == null) {
            Brc b1 = new Brc();
            opr.setEnd(b1);
            b1.then = branch.els = new SyncB().req;
            branch.els.add(opr);
        } else {
            Brc b1 = new Brc();
            opr.setEnd(b1);
            branch.els = new SyncB().req;

            els.translate();
            SyncO opr2 = Dojo.curOpr;
            Brc b2 = new Brc();

            SyncR end = new SyncB().req;
            opr2.setEnd(b2);
            b1.then = b2.then = end;
            end.add(opr);
            end.add(opr2);
        }
        branch.then.add(begin);
        branch.els.add(begin);
        return null;
    }
}
