import meta.Cursor;

public class Lang {
    private final Cursor cursor;
    public Lang(Word wd) {
        cursor = new Cursor(wd.tokens());
    }

}
