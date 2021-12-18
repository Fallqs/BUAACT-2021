package engine;

import engine.sync.*;
import meta.Meta;
import meta.mcode.Call;
import meta.mcode.Put;
import meta.midt.MFunc;
import meta.midt.MTable;
import meta.midt.MVar;

import java.util.*;

public class Dojo {
    private static final ArrayList<SyncB> blks = new ArrayList<>();
    public static GlobalR globalReq = new GlobalR();
    public static GlobalO globalOpr = new GlobalO();
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

    public static int tarjanT = 0;
    public static final Stack<SyncB> stack = new Stack<>();

    public static void tarjan(SyncB u) {
        u.vis = 1;
        u.dfn = u.low = ++tarjanT;
        stack.push(u);
        List<SyncR> lgd = new ArrayList<>(u.opr.legendH);
        lgd.addAll(u.opr.legendL);
        for (SyncR req : lgd) {
            SyncB v = req.blk;
            if (v == null) continue;
            if (v.vis == 0) {
                tarjan(v);
                u.low = Math.min(u.low, v.low);
            } else if (v.vis == 1) {
                u.low = Math.min(u.low, v.dfn);
            }
        }
        if (u.dfn == u.low) {
            while (stack.peek() != u) {
                SyncB v = stack.pop();
                u.sizV += v.valid ? 1 : 0;
                ++u.sizA;
                v.fa = u;
                v.vis = 2;
            }
            u.vis = 2;
            u.sizV += u.valid ? 1 : 0;
            ++u.sizA;
            stack.pop();
        }
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

        for (SyncB blk : blks) new DAG(blk.ms).shrink();

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

        for (SyncB blk : blks) blk.checkValid();
        for (SyncB blk : blks) if (blk.vis == 0) tarjan(blk);
        for (SyncB blk : blks)
            if (blk.valid) blk.fa.foreign = blk.fa;

        status = false;
        while (!status) {
            status = true;
            for (int i = blks.size() - 1; i >= 0; --i) status &= blks.get(i).opr.transValid();
            for (int i = blks.size() - 1; i >= 0; --i) status &= blks.get(i).checkForeign();
        }

        for (SyncB blk : blks)
            if (blk.fa.foreign == blk.fa || blk.fa.valid) blk.valid = true;

        for (SyncB blk : blks) System.out.println(blk.req + " " + (blk.valid) + " fa=" + blk.fa.req);
        System.out.println();

        status = false;
        while (!status) {
            status = true;
            for (int i = blks.size() - 1; i >= 0; --i) status &= blks.get(i).opr.transMeta();
        }

        for (SyncB blk : blks) {
//            if (!blk.valid) continue;
            blk.req.transLive();
            blk.opr.transLive();
        }

        for (MFunc f : MTable.func) f.memAlloc();
//        globalOpr.setReg();

        status = false;
        while (!status) {
            status = true;
            for (SyncB blk : blks)
                for (Meta m : blk.ms)
                    if (m instanceof Call) status &= blk.req.func.updRegUse(((Call) m).func.malloc.used);
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
