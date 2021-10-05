package meta;

import word.Result;
import word.Typ;

import java.util.List;

public class Cursor {
    public final Result[] token;
    public int p = 0;

    public Cursor(List<Result> soup) {
        token = soup.toArray(new Result[0]);
    }

    public boolean isTyp(Typ t) {
        return p >= token.length || token[p].isTyp(t);
    }

    public Cursor nex() {
        p += (p < token.length) ? 1 : 0;
        return this;
    }

    public Cursor las() {
        p -= (p > 0) ? 1 : 0;
        return this;
    }

    public Result cont() {
        return p < 0 || p >= token.length ? null : token[p];
    }
}
