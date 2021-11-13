package meta.midt;

/**
 * output String in printf, stored as .asciiz
 */
public class MStr implements MIdt {
    private static int cnt = 0;
    public int base = 0;
    public final int size;
    private final int id;
    public final String cont;

    public MStr(String s) {
        id = ++cnt;
        cont = s;
        size = s.length() + 1;
        MTable.newIdt(this);
    }


    @Override
    public MTyp typ() {
        return MTyp.String;
    }

    @Override
    public String name() {
        return "str_" + id;
    }

    @Override
    public String toString() {
        return "str" + id + " '" + cont + "'";
    }
}
