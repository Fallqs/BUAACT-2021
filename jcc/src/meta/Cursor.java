package meta;

import word.Result;
import word.Typ;

import java.util.List;

public class Cursor {
    public final Result[] tokens;
    public final Err err;
    public int p = 0;
    public int whl = 0;

    public Cursor(List<Result> soup, Err err) {
        tokens = soup.toArray(new Result[0]);
        this.err = err;
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

    public Cursor chkTil(Typ t) {
        while (!isTyp(t)) nex();
        return this;
    }

    public Cursor chkErr(Typ t) {
        switch (t) {
            case SEMICN:
                if (!isTyp(Typ.SEMICN)) err.add(tokens[p - 1], 'i');
                break;
            case RPARENT:
                if (!isTyp(Typ.RPARENT)) err.add(tokens[p - 1], 'j');
                break;
            case RBRACK:
                if (!isTyp(Typ.RBRACK)) err.add(tokens[p - 1], 'k');
                break;
            case WHILETK:
                if (whl == 0) err.add(tokens[p], 'm');
                break;
            case STRCON:
                err.add(tokens[p], 'a');
                break;
            case PRINTFTK:
                err.add(tokens[p], 'l');
                break;
            case NULLRET:
                err.add(tokens[p-1], 'g');
                break;
            case RETURNTK:
                err.add(tokens[p], 'f');
                break;
            default:
                break;
        }
        return this;
    }

    public Cursor chkErr(Typ t, int pos) {
        int cur = p;
        p = pos;
        chkErr(t);
        p = cur;
        return this;
    }

    public Cursor chkErr(Typ t, Result r) {
        switch (t) {
            case IDENFR:
                err.add(r, 'b');
                break;
            case NULL:
                err.add(r, 'c');
                break;
            case CONSTTK:
                err.add(r, 'h');
                break;
            case PARAMCNT:
                err.add(r, 'd');
                break;
            case PARAMTYP:
                err.add(r, 'e');
                break;
            default:
                break;
        }
        return this;
    }
}
