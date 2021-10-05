package grammar;

public class Compo {
    public final int p;
    public final NTyp typ;

    public Compo(NTyp typ, int p) {
        this.typ = typ;
        this.p = p;
    }

    public String toString() {
        return "<" + typ + ">";
    }
}
