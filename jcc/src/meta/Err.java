package meta;

import word.Grid;
import word.Result;

import java.util.ArrayList;
import java.util.List;

public class Err {
    private final List<Grid> grids;

    private final List<String> cont = new ArrayList<>();

    public Err(List<Grid> g) {
        grids = g;
    }

    public void add(Result r, char c) {
        cont.add(grids.get(r.p).x + " " + c);
    }

    @Override
    public String toString() {
        return String.join("\n", cont);
    }
}
