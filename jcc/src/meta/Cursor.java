package meta;

import word.Result;
import word.Typ;

import java.util.List;

public class Cursor {
    public final Result[] tokens;
    public int p = 0;

    public Cursor(List<Result> soup) {
        tokens = soup.toArray(new Result[0]);
    }

    public boolean isTyp(Typ t) {
        return p >= tokens.length || tokens[p].isTyp(t);
    }

    public Cursor nex() {
        p += (p < tokens.length) ? 1 : 0;
        return this;
    }

    public Cursor las() {
        p -= (p > 0) ? 1 : 0;
        return this;
    }

    public Result cont() {
        return p < 0 || p >= tokens.length ? null : tokens[p];
    }

    public Typ typ() {
        return p < 0 || p >= tokens.length ? Typ.END : tokens[p].typ;
    }

    public int pos() {
        return p < 0 ? 0 : p >= tokens.length ?
                tokens[tokens.length - 1].p + 1 : tokens[p].p;
    }
}
