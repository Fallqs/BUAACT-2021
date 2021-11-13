package engine;

import engine.sync.SyncB;
import engine.sync.SyncO;
import engine.sync.SyncR;
import meta.Meta;
import meta.midt.MFunc;
import meta.midt.MVar;

import java.util.ArrayList;
import java.util.Comparator;

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
        if (curOpr != null) curOpr.upd(v, m);
    }

    public static void index() {
        globalReq.index();
    }

    public static void translate() {
        for (SyncB b : blks) b.opr.handle().forEach(Interpreter::handle);
    }

    public static void sort() {
        for (SyncB blk : blks) blk.ms.sort(Comparator.naturalOrder());;
    }

    public static String toStr() {
        StringBuilder ret = new StringBuilder();
        for (SyncB blk : blks) ret.append(blk);
        return ret.toString();
    }
}
