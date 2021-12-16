package engine.sync;

import engine.Index;
import meta.Meta;
import meta.midt.MFunc;
import meta.midt.MVar;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GlobalO extends SyncO {
    @Override
    public void upd(MVar v, Meta m) {
        mp.put(v, m);
    }

    @Override
    public void setFunc(MFunc f) {
    }

    @Override
    public String toString() {
        return "Global_Opr";
    }

    @Override
    public void translate() {
    }

    @Override
    public int compareTo(Index o) {
        return o == this ? 0 : -1;
    }
}
