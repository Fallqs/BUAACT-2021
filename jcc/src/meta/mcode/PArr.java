package meta.mcode;

import engine.Dojo;
import engine.instr.*;
import meta.Meta;
import meta.midt.MVar;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PArr extends Meta implements Virtual {
    public Meta[] fr;
    public boolean[] ban;
    public final MVar var;

    public PArr(MVar var, Meta... fr) {
        this.var = var;
        this.fr = fr;
        ban = new boolean[fr.length];
        cnst = false;
        var.putv.add(this);
        Dojo.upd(var, this);
        for (Meta m : fr) m.addLegend(this);
        valid = true;
    }

    @Override
    public boolean isCnst() {
        if (cnst) return true;
        for (Meta i : fr) if (!i.isCnst()) return false;
        return cnst = true;
    }

    public void toCnst() {
        if (!isCnst()) return;
        int[] val = new int[fr.length];
        for (int i = 0; i < fr.length; ++i) val[i] = fr[i].calc();
        var.initc(val);
    }

    @Override
    public int calc() {
        return val = var.base;
    }

    @Override
    public Meta[] prevs() {
        List<Meta> ret = new LinkedList<>();
        for (int i = 0; i < fr.length; ++i) if (!ban[i]) ret.add(fr[i]);
        return ret.toArray(new Meta[0]);
    }

    @Override
    public int get(int tmp, int shift) {
        if (Instr.bsR(var) == Instr.GP) new InstrI(Op.addi, tmp, Instr.GP, var.base);
        else if (var.isParam) new InstrLS(Op.lw, tmp, var.base + shift, Instr.SP);
        else new InstrI(Op.addi, tmp, Instr.SP, var.base + shift);
        return tmp;
    }

    @Override
    public int get(int tmp) {
        return get(tmp, 0);
    }

    @Override
    public Instr translate() {
        for (int i = 0; i < fr.length; ++i) {
            new InstrLS(Op.sw, fr[i].get(Instr.V0), var.base + (var.xi(i) << 2), Instr.bsR(var));
        }
        return null;
    }

    @Override
    public String toString() {
        return "PArr " + var.toString() + " " + id;
    }
}
