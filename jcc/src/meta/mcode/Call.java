package meta.mcode;

import engine.Dojo;
import engine.instr.*;
import meta.Meta;
import meta.midt.MFunc;
import meta.midt.MTyp;
import meta.midt.MVar;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Call extends Meta implements Concrete {
    public final MFunc func;
    public Meta[] params;
    public final Map<MVar, Meta> sync;
    public final Map<MVar, Meta> retrieve = new HashMap<>();
    public final Set<Meta> preserve = new HashSet<>();

    public Call(MFunc f, Meta... params) {
        func = f;
        this.params = params;
        sync = Dojo.curOpr.save();
        valid = true;
    }

    @Override
    public boolean isCnst() {
        return cnst = false;
    }

    @Override
    public int calc() {
        return val = 0;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder().append('(');
        for (Meta m : params) str.append(" push T").append(m.id).append('\n');
        return str.append(" Call ").append(func.name).append(')').toString();
    }

    @Override
    public Meta[] prevs() {
        List<Meta> ret = Stream.of(params).collect(Collectors.toList());
        for (Map.Entry<MVar, Meta> e : sync.entrySet())
            if (func.reads.contains(e.getKey())) ret.add(e.getValue());
        return ret.toArray(new Meta[0]);
    }

    @Override
    public Instr translate() {
        sync.entrySet().removeIf(e ->
                !e.getValue().valid || e.getValue() instanceof Put);
        retrieve.entrySet().removeIf(e -> !func.writes.contains(e.getKey()) || !e.getValue().valid);
        for (Map.Entry<MVar, Meta> e : sync.entrySet()) {
            Meta m = e.getValue();
            MVar v = e.getKey();
            if (m instanceof Phi && ((Phi) m).fr.isEmpty()) continue;
            new InstrLS(Op.sw, m.get(Instr.V0), v.base, Instr.GP);
        }
        preserve.removeIf(e -> e.reg < 0 || !func.malloc.used.contains(e.reg) || !e.valid);
        for (Meta m : preserve) new InstrLS(Op.sw, m.reg, m.spx, Instr.SP);

        new InstrI(Op.addi, Instr.SP, Instr.SP, -func.stackSiz);
        RoundRobin.init();
        for (int i = 0; i < params.length; ++i) {
            Meta u = params[i].eqls();
            MVar v = func.params.get(i);
            if (v.reg == -1) new InstrLS(Op.sw, u.get(Instr.V0, func.stackSiz), v.base, Instr.SP);
            else if (u.gtag(Instr.V0) != Instr.V0) {
                RoundRobin.add(u.gtag(Instr.V0), v.reg);
            }
        }
        RoundRobin.alloc();
        for (int i = 0; i < params.length; ++i) {
            Meta u = params[i].eqls();
            MVar v = func.params.get(i);
            if (u.gtag(Instr.V0) == Instr.V0) new InstrDual(Op.move, v.reg, u.get(Instr.V0, func.stackSiz));
        }

        new InstrJ(Op.jal, func.req);
        Instr ret = null;
        if (func.typ() != MTyp.Void) {
            if (reg >= 0) ret = new InstrR(Op.or, reg, Instr.V0, Instr.ZERO);
            else ret = new InstrLS(Op.sw, Instr.V0, spx, Instr.SP);
        }

        for (Map.Entry<MVar, Meta> e : retrieve.entrySet()) {
            Meta m = e.getValue();
            MVar v = e.getKey();
            if (m.reg >= 0) new InstrLS(Op.lw, m.reg, v.base, Instr.GP);
            else {
                new InstrLS(Op.lw, Instr.V0, v.base, Instr.GP);
                new InstrLS(Op.sw, Instr.V0, m.spx, Instr.SP);
            }
        }
        for (Meta m : preserve) if (m.reg >= 0 && !(m instanceof Virtual)) new InstrLS(Op.lw, m.reg, m.spx, Instr.SP);

        return ret;
    }

    @Override
    public boolean be() {
        return true;
    }
}
