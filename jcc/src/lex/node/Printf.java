package lex.node;

import lex.NTyp;
import lex.New;
import lex.Node;
import meta.Meta;
import meta.mcode.Cout;
import meta.midt.MStr;
import word.Result;
import word.Typ;

import java.util.ArrayList;

public class Printf extends Node {
    private Result fmt;
    private ArrayList<Node> exp = new ArrayList<>();
    private int siz = 0;

    public Printf() {
        autoDisplay = false;
    }

    private int cnt() {
        int ans = 0;
        char[] ch = fmt.text.substring(1, fmt.text.length() - 1).toCharArray();
        for (char c : ch) if ((c < 40 || c > 126) && c != 32 && c != 33 && c != '%') return -1;
        if (ch.length > 0 && (ch[ch.length - 1] == '\\' || ch[ch.length - 1] == '%')) return -1;
        for (int i = 0; i < ch.length - 1; ++i) {
            if (ch[i] == '%') {
                if (ch[i + 1] != 'd') return -1;
                ++ans;
                ++i;
            } else if (ch[i] == '\\') {
                if (ch[i + 1] != 'n') return -1;
                ++i;
            }
        }
        return ans;
    }

    /* 'printf' '(' FormatString { ',' Exp } ')' ';' */
    @Override
    public boolean forward() {
        if (!cs.isTyp(Typ.PRINTFTK)) return false;
        int pos = cs.p;
        if (!cs.nex().isTyp(Typ.LPARENT)) return true;
        if (!cs.nex().isTyp(Typ.STRCON)) return true;
        fmt = cs.cont();
        if (-1 == (siz = cnt())) cs.chkErr(Typ.STRCON);
        cs.nex();
        Node ch;
        while (cs.isTyp(Typ.COMMA)) {
            cs.nex();
            if (!(ch = New.typ(NTyp.Exp)).fwd()) break;
            exp.add(ch);
        }
        if (siz != -1 && siz != exp.size()) cs.chkErr(Typ.PRINTFTK, pos);
        cs.chkErr(Typ.RPARENT).nex().chkErr(Typ.SEMICN).nex();
        return true;
    }

    @Override
    public void logIdt() {
        for (Node i : exp) i.logIdt();
    }

    @Override
    public Meta translate() {
        char[] ch = fmt.text.toCharArray();
        for (int l = 1, r = 0, k = 0; r < ch.length - 1; ++r) {
            if (ch[r + 1] == '%' || r == ch.length - 2) {
                if (r >= l) new Cout(new MStr(fmt.text.substring(l, r+1)));
                if (ch[r + 1] == '%') {
                    new Cout(exp.get(k++).translate());
                    ++r;
                }
                l = r + 2;
            }
        }
        return null;
    }
}
