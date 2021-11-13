package engine.instr;

public class Instr {
    public Instr prv, nex;
    public static Instr cur;

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
}
