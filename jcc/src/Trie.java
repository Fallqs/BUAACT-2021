import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Character.isAlphabetic;
import static java.lang.Character.isDigit;

public class Trie {
    private class Result {
        public final WordTyp typ;
        public final int p;
        public String text;

        public Result(WordTyp t, int p) {
            typ = t;
            this.p = p;
        }

        public Result(WordTyp t, int p, String s) {
            text = s;
            typ = t;
            this.p = p;
        }
    }

    private class Nd {
        private final Map<Character, Nd> mp;
        private WordTyp typ;
        private final char c;
        private final Nd fa;

        public Nd(char c, WordTyp typ, Nd fa) {
            mp = new HashMap<>();
            this.typ = typ;
            this.c = c;
            this.fa = fa;
        }

        public boolean isroot() {
            return fa == null;
        }

        public void match(char[] s, int p, WordTyp t) {
            if (p >= s.length) {
                typ = t;
                return;
            }
            Nd ch = mp.get(s[p]);
            if (ch == null) {
                ch = new Nd(s[p], p + 1 == s.length ? t : WordTyp.Error, this);
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

    private final ArrayList<Result> tokens = new ArrayList<>();
    private final Nd root = new Nd('^', WordTyp.Error, null);

    private static final String[] signal0 = {
            "main", "const", "int", "break", "continue", "if", "else",
            "!", "&&", "||", "while", "getint", "printf", "return", "+", "-", "void",
            "*", "/", "%", "<", "<=", ">", ">=", "==", "!=",
            "=", ";", ",", "(", ")", "[", "]", "{", "}"
    };

    private static final WordTyp[] signal1 = {
            WordTyp.MAINTK, WordTyp.CONSTTK, WordTyp.INTTK, WordTyp.BREAKTK,
            WordTyp.CONTINUETK, WordTyp.IFTK, WordTyp.ELSETK,

            WordTyp.NOT, WordTyp.AND, WordTyp.OR, WordTyp.WHILETK, WordTyp.GETINTTK,
            WordTyp.PRINTFTK, WordTyp.RETURNTK, WordTyp.PLUS, WordTyp.MINU, WordTyp.VOIDTK,

            WordTyp.MULT, WordTyp.DIV, WordTyp.MOD, WordTyp.LSS, WordTyp.LEQ,
            WordTyp.GRE, WordTyp.GEQ, WordTyp.EQL, WordTyp.NEQ,

            WordTyp.ASSIGN, WordTyp.SEMICN, WordTyp.COMMA, WordTyp.LPARENT,
            WordTyp.RPARENT, WordTyp.LBRACK, WordTyp.RBRACK, WordTyp.LBRACE, WordTyp.RBRACE
    };

    public Trie(char[] s) {
        for (int i = 0; i < signal0.length; ++i)
            root.match(signal0[i].toCharArray(), 0, signal1[i]);
        // System.out.println(String.valueOf(s));
        work(s);
    }

    private void work(char[] s) {
        for (int p = 0; p < s.length; ) {
            if (s[p] == ' ') ++p;
            else if (s[p] == '"') {
                int q = fmt(s, p);
                if (q == p + 1) tokens.add(new Result(WordTyp.Error, p));
                else tokens.add(new Result(WordTyp.STRCON, q, dump(s, p, q)));
                p = q;
            } else if (isDigit(s[p])) {
                int q = digit(s, p);
                tokens.add(new Result(WordTyp.INTCON, q, dump(s, p, q)));
                p = q;
            } else {
                Result r = root.ident(s, p);
                int q = ident(s, p);
                if (r.typ != WordTyp.Error) {
                    if (q > r.p) {
                        String text = dump(s, p, q);
                        tokens.add(new Result(WordTyp.IDENFR, q, text));
                        root.match(text.toCharArray(), 0, WordTyp.IDENFR);
                        p = q;
                    } else {
                        r.text = dump(s, p, r.p);
                        tokens.add(r);
                        p = r.p;
                    }
                } else {
                    if (p == q) {
                        tokens.add(new Result(WordTyp.Error, p++));
                    } else {
                        String text = dump(s, p, q);
                        root.match(text.toCharArray(), 0, WordTyp.IDENFR);
                        tokens.add(new Result(WordTyp.IDENFR, q, text));
                        p = q;
                    }
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
}
/*
 * 1. delete scripts
 * 2. exclude format strings
 * 3. reserves and idents
 * 4. constants
 */