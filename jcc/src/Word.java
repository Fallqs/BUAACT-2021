import word.Grid;
import word.Result;
import word.Typ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.lang.Character.isAlphabetic;
import static java.lang.Character.isDigit;
import static java.lang.Math.max;

public class Word {
    private static class Nd {
        private final Map<Character, Nd> mp;
        private Typ typ;
        private final char c;
        private final Nd fa;

        public Nd(char c, Typ typ, Nd fa) {
            mp = new HashMap<>();
            this.typ = typ;
            this.c = c;
            this.fa = fa;
        }

        public boolean isroot() {
            return fa == null;
        }

        public void match(char[] s, int p, Typ t) {
            if (p >= s.length) {
                typ = t;
                return;
            }
            Nd ch = mp.get(s[p]);
            if (ch == null) {
                ch = new Nd(s[p], p + 1 == s.length ? t : Typ.Error, this);
                mp.put(s[p], ch);
            }
            ch.match(s, p + 1, t);
        }

        public Result ident(char[] s, int p) {
            Nd ch = mp.get(s[p]);
            if (ch == null) return new Result(typ, p);
            return ch.ident(s, p + 1);
        }
    }

    private int digit(char[] s, int p) {
        while (isDigit(s[p])) ++p;
        return p;
    }

    private int ident(char[] s, int p) {
        if (s[p] != '_' && !isAlphabetic(s[p])) return p;
        ++p;
        while (s[p] == '_' || isDigit(s[p]) || isAlphabetic(s[p])) ++p;
        return p;
    }

    private int fmt(char[] s, int p) {
        if (s[p] != '"') return p;
        int q = ++p;
        while (p < s.length && s[p] != '"') ++p;
        return p == s.length ? q : p + 1;
    }

    private String dump(char[] s, int p, int q) {
        StringBuilder ans = new StringBuilder();
        for (int i = p; i != q; ++i) ans.append(s[i]);
        return ans.toString();
    }

    private final List<Result> tokens = new LinkedList<>();
    private final Nd root = new Nd('^', Typ.Error, null);

    private static final String[] signal0 = {
            "main", "const", "int", "break", "continue", "if", "else",
            "!", "&&", "||", "while", "getint", "printf", "return", "+", "-", "void",
            "*", "/", "%", "<", "<=", ">", ">=", "==", "!=",
            "=", ";", ",", "(", ")", "[", "]", "{", "}"
    };

    private static final Typ[] signal1 = {
            Typ.MAINTK, Typ.CONSTTK, Typ.INTTK, Typ.BREAKTK,
            Typ.CONTINUETK, Typ.IFTK, Typ.ELSETK,

            Typ.NOT, Typ.AND, Typ.OR, Typ.WHILETK, Typ.GETINTTK,
            Typ.PRINTFTK, Typ.RETURNTK, Typ.PLUS, Typ.MINU, Typ.VOIDTK,

            Typ.MULT, Typ.DIV, Typ.MOD, Typ.LSS, Typ.LEQ,
            Typ.GRE, Typ.GEQ, Typ.EQL, Typ.NEQ,

            Typ.ASSIGN, Typ.SEMICN, Typ.COMMA, Typ.LPARENT,
            Typ.RPARENT, Typ.LBRACK, Typ.RBRACK, Typ.LBRACE, Typ.RBRACE
    };

    public Word(ArrayList<Grid> s) {
        for (int i = 0; i < signal0.length; ++i)
            root.match(signal0[i].toCharArray(), 0, signal1[i]);
        char[] chars = new char[s.size()];
        for (int i = 0; i < chars.length; ++i) chars[i] = s.get(i).c;
        work(chars);
    }

    private void work(char[] s) {
        for (int p = 0; p < s.length; ) {
            if (s[p] == ' ') ++p;
            else if (s[p] == '"') {
                int q = fmt(s, p);
                if (q == p + 1) tokens.add(new Result(Typ.Error, p));
                else tokens.add(new Result(Typ.STRCON, q, dump(s, p, q)));
                p = q;
            } else if (isDigit(s[p])) {
                int q = digit(s, p);
                tokens.add(new Result(Typ.INTCON, q, dump(s, p, q)));
                p = q;
            } else {
                Result r = root.ident(s, p);
                int q = ident(s, p);
                if (r.typ == Typ.Error || q > r.p) {
                    String text = dump(s, p, q);
                    tokens.add(new Result(p == q ? Typ.Error : Typ.IDENFR, q, text));
                    p = max(q, p + 1);
                } else {
                    r.text = dump(s, p, r.p);
                    tokens.add(r);
                    p = max(r.p, p + 1);
                }
            }
        }
    }

    public String toString() {
        StringBuilder ret = new StringBuilder();
        for (Result u : tokens) {
            ret.append(u.typ).append(" ").append(u.text).append("\n");
        }
        return ret.toString();
    }

    public List<Result> tokens() {
        return tokens;
    }
}
/*
 * 1. delete scripts
 * 2. exclude format strings
 * 3. reserves and idents
 * 4. constants
 */