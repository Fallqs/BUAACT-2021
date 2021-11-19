package meta.mcode;

import engine.instr.Instr;
import engine.instr.InstrLS;
import engine.instr.InstrR;
import engine.instr.Op;
import meta.Meta;
import meta.midt.MVar;

import java.util.ArrayList;
import java.util.List;

/**
 * get value from RAM / Global $, component of SyncR
 */
public class Get extends Meta {
    public MVar var;
    public List<Meta> putv;

    public Get(MVar var) {
        super(false);
        this.var = var;
        putv = new ArrayList<>(var.putv);
    }

    @Override
    public boolean isCnst() {
        return cnst = var.cnst;
    }

    @Override
    public int calc() {
        return var.gval();
    }

    @Override
    public String toString() {
        return "(" + var.name + " -> T" + this.id + ")";
    }

    @Override
    public void collect() {
        if (ref == 0) for (Meta m : putv) m.collect();
        super.collect();
    }

    @Override
    public Meta[] prevs() {
        return putv.toArray(new Meta[0]);
    }

    @Override
    public Instr translate() {
//        Instr ret;
//        if (reg >= 0) ret = new InstrLS(Op.lw, reg, var.base, Instr.bsR(var));
//        else {
//            new InstrLS(Op.lw, Instr.V0, var.base, Instr.bsR(var));
//            ret = new InstrLS(Op.sw, Instr.V0, spx, Instr.SP);
//        }
//        return ret;
        return null;
    }
}
