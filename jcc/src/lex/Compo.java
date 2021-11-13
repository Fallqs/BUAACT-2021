package lex;

public class Compo implements Comparable<Compo> {
    public final int p;
    public final NTyp typ;
    public int tim = 0;

    public Compo(NTyp typ, int p) {
        this.typ = typ;
        this.p = p;
    }

    public String toString() {
        return "<" + typ + ">";
    }

    @Override
    public int compareTo(Compo o) {
        return p != o.p ? Integer.compare(p, o.p) : Integer.compare(tim, o.tim);
    }
}
