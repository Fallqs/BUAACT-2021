package engine;

import meta.Meta;
import meta.midt.MFunc;
import meta.midt.MVar;

import java.util.ArrayList;

public class Dojo {
//    private static final ArrayList<SyncR> reqs = new ArrayList<>();
//    private static final ArrayList<SyncO> oprs = new ArrayList<>();
    private static final ArrayList<SyncB> blks = new ArrayList<>();
    public static SyncR globalReq = new SyncR();
    public static SyncR curReq;
    public static SyncO curOpr;
    public static SyncB curB;
    public static MFunc curFunc;

    public static void add(Index ix) {
        if (ix instanceof SyncR) {
//            if (curReq != ix) reqs.add(curReq = (SyncR) ix);
            curReq = (SyncR) ix;
        } else {
//            if (curOpr != ix) oprs.add(curOpr = (SyncO) ix);
            curOpr = (SyncO) ix;
        }
    }

    public static void popReq() {
        curReq = null;
    }

    public static void popOpr() {
        curOpr = null;
    }

    public static void add(SyncB blk) {
        blks.add(curB = blk);
    }

    public static void embed(Meta m) {
        if (curB != null) curB.embed(m);
    }

    public static void clean() {
        globalReq.add(curOpr);
        curOpr.setEnd();
        curFunc = null;
        curReq = null;
        curOpr = null;
        curB = null;
    }

    public static Meta qry(MVar v) {
        return curOpr.qry(v);
    }

    public static void upd(MVar v, Meta m) {
        curOpr.upd(v, m);
    }

    public static void index() {
        globalReq.index();
    }

    public static void translate() {
        for (SyncB b : blks) b.opr.handle().forEach(Interpreter::handle);
    }

    public static String toStr() {
        StringBuilder ret = new StringBuilder();
        for(SyncB blk : blks) ret.append(blk);
        return ret.toString();
    }
}
