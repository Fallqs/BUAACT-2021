package engine.instr;

import engine.MetaAlloc;
import meta.midt.MVar;

public class Instr {
    public Instr prv, nex;
    public static Instr cur;
    public Op op;

    public Instr() {
        if (cur != null) {
            prv = cur;
            cur.nex = this;
        }
        cur = this;
    }

    public void addPrev(Instr n) {
        if (n == null) return;
        (n.prv = this.prv).nex = n;
        (this.prv = n).nex = this;
    }

    public void addNex(Instr n) {
        if (n == null) return;
        (n.nex = this.nex).prv = n;
        (this.nex = n).prv = this;
    }

    public void reMov() {
        if (nex != null) nex.prv = prv;
        if (prv != null) prv.nex = nex;
    }

    public static final int V0 = -2, A0 = -3, RA = -4, SP = -5, GP = -6, ZERO = -7;

    public static String getReg(int x) {
        if (x >= 0) return "$" + MetaAlloc.regs[x];
        switch (x) {
            case V0:
                return "$v0";
            case A0:
                return "$a0";
            case RA:
                return "$ra";
            case SP:
                return "$sp";
            case GP:
                return "$gp";
            default:
                return "$0";
        }
    }

    public static int bsR(MVar v) {
        return v.cnst || v.global ? GP : SP;
    }

    public Op getOp() {
        return op;
    }
}
