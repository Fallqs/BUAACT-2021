import node.Cursor;

import java.util.List;

public class Lang {
    private Cursor cursor;
    public Lang(Word wd) {
        cursor = new Cursor(wd.tokens());
    }

}
