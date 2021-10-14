import grammar.Compo;
import meta.Err;
import grammar.NTyp;
import grammar.New;
import grammar.Node;
import meta.Cursor;
import word.Result;

import java.util.Arrays;
import java.util.List;

public class Lang {
    private List<Compo> tokens = Node.output;
    private Node unit;

    public Lang(Word wd, Err err) {
        Node.cs = new Cursor(wd.tokens(), err);
        (unit = New.typ(NTyp.CompUnit)).fwd();
    }

    public String toString() {
        Compo[] cpo = tokens.toArray(new Compo[0]);
        Arrays.sort(cpo);
        Result[] rst = Node.cs.tokens;
        int i = 0, j = 0;
        StringBuilder ret = new StringBuilder();
        while (i < cpo.length || j < rst.length) {
            if (i< cpo.length && (j >= rst.length || cpo[i].p <= rst[j].p)) ret.append(cpo[i++]).append('\n');
            else ret.append(rst[j++]).append('\n');
        }
        return ret.toString();
//        return Node.cs.err.toString();
    }

}
