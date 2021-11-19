package engine;

import engine.sync.GlobalR;
import engine.sync.SyncB;
import engine.sync.SyncO;
import engine.sync.SyncR;
import meta.Meta;
import meta.midt.MFunc;
import meta.midt.MTable;
import meta.midt.MVar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

public class Dojo {
    private static final ArrayList<SyncB> blks = new ArrayList<>();
    public static SyncR globalReq = new GlobalR();
    public static SyncR curReq;
    public static SyncO curOpr;
    public static SyncB curB;
    public static MFunc curFunc;

    /**
     * Sync : add(), clean()
     * Meta : embed(), qry(), upd()
     */
    public static void add(Index ix) {
        if (ix instanceof SyncR) {
            curReq = (SyncR) ix;
        } else {
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

    /**
     * Translating : sort(), index(), translate()
     */
    public static void index() {
        for (MVar v : MTable.global) globalReq.qry(v);
        for (Meta m : globalReq.mp.values()) m.valid = true;
        for (MFunc f : MTable.func) {
            f.req.setFunc(f);
            f.req.indexOpr(new HashMap<>());
            f.req.flushCnt();
            f.req.indexPhi();
            f.req.flushCnt();
            f.req.indexPhi();
            f.req.flushCnt();
        }
        globalReq.indexMeta(new HashSet<>());
        for (MFunc f : MTable.func) {
            f.memAlloc();
            f.req.flushCnt();
        }
    }

    public static void translate() {
        sort();
        index();
    }

    public static void sort() {
        for (SyncB blk : blks) blk.ms.sort(Comparator.naturalOrder());
    }

    public static String toStr() {
        StringBuilder ret = new StringBuilder();
        for (SyncB blk : blks) ret.append(blk);
        return ret.toString();
    }
}
