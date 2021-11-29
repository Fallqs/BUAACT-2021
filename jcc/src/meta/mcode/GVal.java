package meta.mcode;

import engine.instr.Instr;
import engine.instr.InstrI;
import engine.instr.InstrLS;
import engine.instr.InstrR;
import engine.instr.Op;
import meta.Meta;
import meta.midt.MVar;

/**
 * load value from HEAP
 */
public class GVal extends Meta {
    public final MVar var;
    protected Meta[] ms;

    public GVal(MVar var, Meta... ms) {
        this.var = var;
        this.ms = ms;
        for (Meta m : ms) m.addLegend(this);
    }

    @Override
    public boolean isCnst() {
        if (!var.cnst) return false;
        for (Meta m : ms) if (!m.isCnst()) return false;
        return true;
    }

    @Override
    public int calc() {
        int[] ix = new int[ms.length];
        for (int i = 0; i < ms.length; ++i) {
            ix[i] = ms[i].calc();
        }
        return var.gval(ix);
    }

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder();
        ans.append("(").append(var.name);
        for (Meta mi : ms) {
            ans.append("[T").append(mi.id).append("]");
        }
        ans.append("@shift=").append(var.lgt).append(" -> T").append(this.id).append(")");
        return ans.toString();
    }

    @Override
    public Meta[] prevs() {
        return ms;
    }

    @Override
    public Instr translate() {
        for (int i = 0; i < ms.length; ++i) ms[i] = ms[i].eqls();
        int tar = gtag(Instr.V0);
        Instr ret;
        switch (var.typ) {
            case Int:
                ret = new InstrLS(Op.lw, tar, var.base, Instr.bsR(var));
                break;
            case Arr:
                if (ms.length == 0) {
                    ret = var.isParam ? new InstrI(Op.addi, tar, Instr.bsR(var), var.base) :
                            new InstrLS(Op.lw, tar, var.base, Instr.SP);
                } else if (!var.isParam) {
                    new InstrI(Op.sll, Instr.V0, ms[0].get(Instr.V0), 2);
                    new InstrR(Op.addu, Instr.V0, Instr.V0, Instr.bsR(var));
                    ret = new InstrLS(Op.lw, tar, var.base, Instr.V0);
                } else {
                    new InstrLS(Op.lw, Instr.V0, var.base, Instr.SP);
                    new InstrI(Op.sll, Instr.A0, ms[0].get(Instr.A0), 2);
                    new InstrR(Op.addu, Instr.V0, Instr.V0, Instr.A0);
                    ret = new InstrLS(Op.lw, tar, 0, Instr.V0);
                }
                break;
            case Mat:
                if (ms.length == 0) {
                    ret = var.isParam ? new InstrI(Op.addi, tar, Instr.bsR(var), var.base) :
                            new InstrLS(Op.lw, tar, var.base, Instr.SP);
                } else if (ms.length == 1) {
                    if (!var.isParam) new InstrI(Op.addi, Instr.V0, Instr.bsR(var), var.base);
                    else new InstrLS(Op.lw, Instr.V0, var.base, Instr.SP);
                    new InstrI(Op.sll, Instr.A0, ms[0].get(Instr.A0), var.lgt + 2);
                    ret = new InstrR(Op.addu, tar, Instr.V0, Instr.A0);
                } else if (!var.isParam) {
                    new InstrI(Op.sll, Instr.V0, ms[0].get(Instr.A0), var.lgt + 2);
                    new InstrI(Op.sll, Instr.A0, ms[1].get(Instr.A0), 2);
                    new InstrR(Op.addu, Instr.V0, Instr.V0, Instr.A0);
                    new InstrR(Op.addu, Instr.V0, Instr.V0, Instr.bsR(var));
                    ret = new InstrLS(Op.lw, tar, var.base, Instr.V0);
                } else {
                    new InstrLS(Op.lw, Instr.V0, var.base, Instr.SP);
                    new InstrI(Op.sll, Instr.A0, ms[0].get(Instr.A0), var.lgt + 2);
                    new InstrR(Op.addu, Instr.V0, Instr.V0, Instr.A0);
                    new InstrI(Op.sll, Instr.A0, ms[1].get(Instr.A0), 2);
                    new InstrR(Op.addu, Instr.V0, Instr.V0, Instr.A0);
                    ret = new InstrLS(Op.lw, tar, 0, Instr.V0);
                }
                break;
            default:
                return null;
        }
        if (tar == Instr.V0) ret = new InstrLS(Op.sw, tar, spx, Instr.SP);
        setSave(tar);
        return ret;
    }
}
