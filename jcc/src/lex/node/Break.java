package lex.node;

import engine.Dojo;
import engine.sync.SyncB;
import lex.NTyp;
import lex.Node;
import meta.Meta;
import meta.mcode.BrGoto;
import word.Typ;

public class Break extends Node {
    public Break() {
        typ = NTyp.Break;
        autoDisplay = false;
    }

    /* 'break' ';' */
    @Override
    public boolean forward() {
        if (!cs.isTyp(Typ.BREAKTK)) return false;
        cs.chkErr(Typ.WHILETK).nex().chkErr(Typ.SEMICN).nex();
        return true;
    }

    @Override
    public Meta translate() {
        Meta ret = new BrGoto(breakPin.peek(), true);
        Dojo.curOpr.setEnd(ret);
        breaks.push(Dojo.curOpr);
//        new SyncB();
//        Dojo.curReq.add(breaks.peek());
        return ret;
    }
}
