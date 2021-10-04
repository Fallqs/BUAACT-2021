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
        return token[p].isTyp(t);
    }
}
