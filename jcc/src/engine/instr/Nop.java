package engine.instr;

public class Nop extends Instr {
    private static int cnt = 0;
    private final int id;
    public String name;
    private boolean pure = false;

    public Nop() {
        id = ++cnt;
        op = Op.nop;
    }

    public Nop(String name) {
        id = ++cnt;
        this.name = name;
        op = Op.nop;
    }

    public Nop(String name, boolean pure) {
        this(name);
        this.pure = pure;
        op = Op.nop;
    }

    @Override
    public String toString() {
        return name == null ? "nop" + id + ":" : pure ? name : name + ":";
    }
}
