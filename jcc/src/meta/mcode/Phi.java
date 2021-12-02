package meta.mcode;

import engine.instr.Instr;
import engine.instr.InstrI;
import engine.instr.InstrLS;
import engine.instr.Op;
import meta.Meta;
import meta.midt.MTyp;
import meta.midt.MVar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Phi extends Meta {
    public MVar var;
    public final List<Meta> fr = new ArrayList<>();

    public Phi(MVar v) {
        super(false);
        this.var = v;
//        System.out.println(this);
    }

    public Phi addFr(Meta m) {
        fr.add(m);
        return this;
    }

    @Override
    public int calc() {
        return val = var.gval();
    }

    @Override
    public boolean isCnst() {
        return cnst = false;
    }

    @Override
    public void shrink() {
        if (fr.isEmpty()) return;
        fr.removeIf(e -> e.eqls() == this);
        boolean single = true;
        for (Meta m : fr)
            if (m.eqls() != fr.get(0).eqls()) {
                single = false;
                break;
            }
        if (single) this.eqls = fr.isEmpty() ? Nop : fr.get(0).eqls();
    }

    @Override
    public String toString() {
        return " Phi" + id + "(" + var.name + ')';
    }

    @Override
    public Meta[] prevs() {
        Set<Meta> ret = new HashSet<>();
        for (Meta m : fr) ret.add(m.eqls());
        return ret.toArray(new Meta[0]);
    }

    @Override
    public int get(int tmp, int shift) {
        if (var.typ != MTyp.Int) {
            if (Instr.bsR(var) == Instr.GP) new InstrI(Op.addi, tmp, Instr.GP, var.base);
            else new InstrI(Op.addi, tmp, Instr.SP, var.base + shift);
        } else {
            if (Instr.bsR(var) == Instr.GP) new InstrLS(Op.lw, tmp, var.base, Instr.bsR(var));
            else new InstrLS(Op.lw, tmp, var.base + shift, Instr.bsR(var));
        }
        return tmp;
    }

    @Override
    public Instr translate() {
        Instr ret;
        if (reg >= 0) {
            ret = new InstrLS(Op.lw, reg, var.base, Instr.bsR(var));
            setSave(reg);
        } else {
            new InstrLS(Op.lw, Instr.V0, var.base, Instr.bsR(var));
            setSave(Instr.V0);
            ret = new InstrLS(Op.sw, Instr.V0, spx, Instr.SP);
        }
        return ret;
    }

    public void save() {
        if (reg >= 0) setSave(reg);
        else setSave(Instr.V0);
    }
}
