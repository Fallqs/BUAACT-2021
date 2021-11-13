package engine.instr;

public class Array extends Instr {
    private final String name;
    private final int siz;
    private final int[] vals;

    public Array(String name, int siz, int... vals) {
        this.name = name;
        this.siz = Math.max(siz, vals.length);
        this.vals = vals;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("var_").append(name);
        if (vals.length < siz) return ret.append(": .space ").append(siz << 2).toString();
        ret.append(": .word");
        if (siz < 8) {
            ret.append(' ');
            for (int i : vals) ret.append(i).append(' ');
        } else {
            ret.append('\n');
            for (int i = 0; i < siz; ++i) ret.append(vals[i]).append(i % 30 == 29 ? '\n' : ' ');
        }
        return ret.toString();
    }
}
