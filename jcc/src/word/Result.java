package word;

public class Result {
    public final Typ typ;
    public final int p;
    public String text;

    public Result(Typ t, int p) {
        typ = t;
        this.p = p;
    }

    public Result(Typ t, int p, String s) {
        text = s;
        typ = t;
        this.p = p;
    }

    public boolean isTyp(Typ t) {
        return typ == t;
    }

    public String toString() {
        return typ.toString() + ' ' + text;
    }
}

