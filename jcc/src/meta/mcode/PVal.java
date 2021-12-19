package meta.mcode;

import engine.Dojo;
import engine.instr.Instr;
import engine.instr.InstrI;
import engine.instr.InstrLS;
import engine.instr.InstrR;
import engine.instr.Op;
import meta.Meta;
import meta.midt.MTyp;
import meta.midt.MVar;

import java.util.Arrays;

public class PVal extends Meta implements Virtual, Concrete {
    public Meta fr;
    public MVar var;
    protected Meta[] ms;

    public PVal(Meta fr, MVar var, Meta... ms) {
        asLegend(this.fr = fr);
        for (Meta m : ms) m.addLegend(this);
        this.var = var;
        this.ms = ms;
        Dojo.curFunc.write(Dojo.curOpr);
        valid = true;
    }

    @Override
    public boolean isCnst() {
        return cnst = fr.isCnst();
    }

    @Override
    public int calc() {
        return val = fr.calc();
    }

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder();
        ans.append("(T").append(fr.id).append(" -> ").append(var.name);
        for (Meta mi : ms) {
            ans.append("[T").append(mi.id).append("]");
        }
        ans.append(", shift=").append(var.lgt).append(")");
        return ans.toString();
    }

    @Override
    public Meta[] prevs() {
        Meta[] ret = Arrays.copyOf(ms, ms.length + (var.param != null ? 2 : 1));
        ret[ret.length - 1] = fr;
        if (var.param != null) ret[ret.length - 2] = var.param;
        return ret;
    }

    private int fetch(int tar) {
        int base = var.reg;
        if (base == -1) {
            new InstrLS(Op.lw, tar, var.base, Instr.SP);
            base = tar;
        }
        return base;
    }

    @Override
    public Instr translate() {
        for (int i = 0; i < ms.length; ++i) ms[i] = ms[i].eqls();
        fr = fr.eqls();
        if (var.typ == MTyp.Int) {
            return new InstrLS(Op.sw, fr.get(Instr.V0), var.base, Instr.bsR(var));
        } else if (var.typ == MTyp.Arr) {
            if (!var.isParam) {
                new InstrI(Op.sll, Instr.A0, ms[0].get(Instr.A0), 2);
                new InstrR(Op.addu, Instr.A0, Instr.A0, Instr.bsR(var));
                return new InstrLS(Op.sw, fr.get(Instr.V0), var.base, Instr.A0);
            } else {
                int base = fetch(Instr.A0);
                new InstrI(Op.sll, Instr.V0, ms[0].get(Instr.V0), 2);
                new InstrR(Op.addu, Instr.A0, base, Instr.V0);
                return new InstrLS(Op.sw, fr.get(Instr.V0), 0, Instr.A0);
            }
        } else if (var.typ == MTyp.Mat) {
            new InstrI(Op.sll, Instr.A0, ms[0].get(Instr.A0), var.lgt + 2);
            new InstrI(Op.sll, Instr.V0, ms[1].get(Instr.V0), 2);
            new InstrR(Op.addu, Instr.A0, Instr.A0, Instr.V0);
            if (!var.isParam) {
                new InstrR(Op.addu, Instr.A0, Instr.A0, Instr.bsR(var));
//                if (!var.global && !var.cnst) new InstrR(Op.addu, Instr.V0, Instr.V0, Instr.SP);
                return new InstrLS(Op.sw, fr.get(Instr.V0), var.base, Instr.A0);
            } else {
                int base = fetch(Instr.V0);
                new InstrR(Op.addu, Instr.A0, Instr.A0, base);
                return new InstrLS(Op.sw, fr.get(Instr.V0), 0, Instr.A0);
            }
        }
        return null;
    }

    @Override
    public boolean be() {
        return true;
    }
}
