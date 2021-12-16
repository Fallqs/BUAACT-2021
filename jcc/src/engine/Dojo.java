package engine;

import engine.sync.*;
import meta.Meta;
import meta.mcode.Put;
import meta.midt.MFunc;
import meta.midt.MTable;
import meta.midt.MVar;

import java.util.*;

public class Dojo {
    private static final ArrayList<SyncB> blks = new ArrayList<>();
    public static SyncR globalReq = new GlobalR();
    public static SyncO globalOpr = new GlobalO();
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
        for (MVar v : MTable.global) globalOpr.upd(v, new Put(v));
        for (MFunc f : MTable.func) {
            for (MVar v : f.params) globalOpr.upd(v, new Put(v));
            globalOpr.addLegend(f.req);
            f.req.setFunc(f);
        }

        boolean status = false;
        while (!status) {
            status = globalOpr.transOpr();
            for (SyncB blk : blks) status &= blk.opr.transOpr();
        }

        status = false;
        while (!status) {
            status = globalReq.transPhi();
            for (SyncB blk : blks) status &= blk.req.transPhi();
        }

        status = false;
        while (!status) {
            status = true;
            for (SyncB blk : blks) status &= blk.opr.transMeta();
        }

        for (SyncB blk : blks) {
            blk.req.transLive();
            blk.opr.transLive();
        }

//        globalOpr.flushCnt();
//        globalOpr.indexOpr(new TreeMap<>(), false);
//        globalOpr.flushCnt();
//        globalOpr.indexPhi();
//        globalOpr.flushCnt();
//        globalOpr.indexPhi();
//        globalOpr.flushCnt();
//        globalReq.indexMeta(new TreeSet<>(), false, false);
//        globalOpr.flushCnt();
//        globalReq.indexMeta(new TreeSet<>(), false, true);
        globalOpr.flushCnt();
        for (MFunc f : MTable.func) f.memAlloc();
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
