package lex.node;

import engine.Dojo;
import engine.sync.SyncB;
import lex.NTyp;
import lex.Node;
import meta.Meta;
import meta.mcode.BrGoto;
import word.Typ;

public class Continue extends Node {
    public Continue() {
        typ = NTyp.Continue;
        autoDisplay = false;
    }

    /* 'continue' ';' */
    @Override
    public boolean forward() {
        if (!cs.isTyp(Typ.CONTINUETK)) return false;
        cs.chkErr(Typ.WHILETK).nex().chkErr(Typ.SEMICN).nex();
        return true;
    }

    @Override
    public Meta translate() {
        Meta ret = new BrGoto(continuePin.peek(), false);
        Dojo.curOpr.setEnd(ret);
        continues.push(Dojo.curOpr);
//        new SyncB();
//        Dojo.curReq.add(continues.peek());
        return ret;
    }
}
