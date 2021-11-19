package engine.sync;

import engine.Dojo;
import meta.Meta;

import java.util.ArrayList;

public class SyncB {
    public final SyncR req = new SyncR(this);
    public final SyncO opr = new SyncO(this);
    public final ArrayList<Meta> ms = new ArrayList<>();
    private static int cnt = 0;
    public final int id;

    public SyncB() {
        Dojo.add(req);
        Dojo.add(opr);
        Dojo.add(this);
        id = ++cnt;
    }

    public void embed(Meta m) {
        ms.add(m);
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        if (req.func != null && req.func.req == req) ret.append('\n').append(req.func);
        ret.append('\n').append(req).append(":\n");
        for (Meta m : req.mp.values()) if (m.valid) ret.append(m).append('\n');
        for (Meta m : ms) ret.append(m).append('\n');
        ret.append(opr.end).append('\n');
        return ret.toString();
    }
}
