package meta.midt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class MTable {
    public static final Map<String, Stk> cur = new HashMap<>();
    public static final List<MIdt> global = new ArrayList<>();
    public static final List<MIdt> temp = new ArrayList<>();
    public static final List<MFunc> func = new ArrayList<>();
    private static final Stack<String> log = new Stack<>();
    private static int nw = -1;

    private static class Stk {
        private static final Stack<MIdt> idt = new Stack<>();
        private static final Stack<Integer> tag = new Stack<>();

        public Stk() {
        }

        public boolean push(MIdt x) {
            if (!tag.empty() && tag.peek().equals(nw)) return false;
            idt.push(x);
            tag.push(nw);
            return true;
        }

        public void pop() {
            if (tag.empty()) return;
            tag.pop();
            idt.pop();
        }

        public MIdt peek() {
            return idt.peek();
        }
    }

    public static void newBlock() {
        log.push("$");
        ++nw;
    }

    public static void popBlock() {
        while (!log.empty()) {
            if ("$".equals(log.peek())) break;
            cur.get(log.pop()).pop();
        }
        --nw;
    }

    public static boolean newIdt(MIdt x) {
        if (!cur.containsKey(x.name())) cur.put(x.name(), new Stk());
        if (!cur.get(x.name()).push(x)) return false;
        if (x.typ() == MTyp.Func) func.add((MFunc) x);
        else if (0 == nw) global.add(x);
        else temp.add(x);
        return true;
    }

    public static MIdt qryIdt(String name) {
        if (!cur.containsKey(name)) return null;
        return cur.get(name).peek();
    }
}
