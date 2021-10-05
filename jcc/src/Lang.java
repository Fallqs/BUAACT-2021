import grammar.Compo;
import meta.Cursor;

import java.util.ArrayList;
import java.util.List;

public class Lang {
    private final Cursor cursor;
    private List<Compo> tokens;

    public Lang(Word wd) {
        cursor = new Cursor(wd.tokens());
        tokens = new ArrayList<>();
    }

}
