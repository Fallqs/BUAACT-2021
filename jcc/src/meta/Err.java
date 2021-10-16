package meta;

import word.Grid;
import word.Result;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.Collections.sort;

public class Err {
    private final List<Grid> grids;

    private class Nd implements Comparable<Nd> {
        public int line;
        public char c;

        public Nd(int line, char c) {
            this.line = line;
            this.c = c;
        }

        public int getLine() {
            return line;
        }

        @Override
        public int compareTo(Nd o) {
            return Integer.compare(o.c, c);
        }

        public String toString() {
            return line + " " + c;
        }

    }

    private final List<Nd> cont = new ArrayList<>();

    public Err(List<Grid> g) {
        grids = g;
    }

    public void add(Result r, char c) {
        cont.add(new Nd(grids.get(r.p).x, c));
    }

    @Override
    public String toString() {
        cont.sort(Comparator.comparing(Nd::getLine));
        StringBuilder ans = new StringBuilder();
        for (Nd n : cont) ans.append(n).append("\n");
        return ans.toString();
    }
}
