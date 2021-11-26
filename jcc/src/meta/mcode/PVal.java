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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PVal extends Meta {
    public Meta fr;
    public MVar var;
    protected Meta[] ms;

    public PVal(Meta fr, MVar var, Meta... ms) {
        asLegend(this.fr = fr);
        for (Meta m : ms) m.addLegend(this);
        this.var = var;
        this.ms = ms;
        Dojo.curFunc.write(Dojo.curOpr);
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
        ans.append("(").append(fr.id).append("->").append(var.name);
        for (Meta mi : ms) {
            ans.append("[T").append(mi.id).append("]");
        }
        ans.append(", shift=").append(var.lgt).append(")");
        return ans.toString();
    }

    @Override
    public Meta[] prevs() {
        Meta[] ret = Arrays.copyOf(ms, ms.length + 1);
        ret[ret.length - 1] = fr;
        return ret;
    }

    @Override
    public Instr translate() {
        if (var.typ == MTyp.Int) {
            return new InstrLS(Op.sw, fr.get(Instr.V0), var.base, Instr.bsR(var));
        } else if (var.typ == MTyp.Arr) {
            if (!var.param) {
                new InstrI(Op.sll, Instr.A0, ms[0].get(Instr.A0), 2);
                new InstrR(Op.add, Instr.A0, Instr.A0, Instr.bsR(var));
                return new InstrLS(Op.sw, fr.get(Instr.V0), var.base, Instr.A0);
            } else {
                new InstrLS(Op.lw, Instr.A0, var.base, Instr.SP);
                new InstrI(Op.sll, Instr.V0, ms[0].get(Instr.V0), 2);
                new InstrR(Op.add, Instr.A0, Instr.A0, Instr.V0);
                return new InstrLS(Op.sw, fr.get(Instr.V0), 0, Instr.A0);
            }
        } else if (var.typ == MTyp.Mat) {
            new InstrI(Op.sll, Instr.A0, ms[0].get(Instr.A0), var.lgt + 2);
            new InstrI(Op.sll, Instr.V0, ms[0].get(Instr.V0), 2);
            new InstrR(Op.add, Instr.A0, Instr.A0, Instr.V0);
            if (!var.param) {
                new InstrR(Op.add, Instr.A0, Instr.A0, Instr.bsR(var));
                return new InstrLS(Op.sw, fr.get(Instr.V0), var.base, Instr.A0);
            } else {
                new InstrLS(Op.lw, Instr.V0, var.base, Instr.SP);
                new InstrR(Op.add, Instr.A0, Instr.A0, Instr.V0);
                return new InstrLS(Op.sw, fr.get(Instr.V0), 0, Instr.A0);
            }
        }
        return null;
    }
}
