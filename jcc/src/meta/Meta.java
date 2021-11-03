package meta;

public class Meta {
    public final Opr opr;
    public final Meta ma, mb;
    public final int tar;
    private static int cnt = 0;

    public Meta(Opr opr, Meta ma, Meta mb) {
        this.opr = opr;
        this.ma = ma;
        this.mb = mb;
        this.tar = ++cnt;
    }

    public String toString() {
        return "(" + opr + ", " + ma.tar + ", " + mb.tar + " => " + tar + ")";
    }
}
