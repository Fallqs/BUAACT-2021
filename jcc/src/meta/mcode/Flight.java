package meta.mcode;

import engine.sync.SyncR;
import meta.Meta;

import java.util.ArrayList;
import java.util.Map;

public interface Flight {
    void addPsi(Map<SyncR, ArrayList<Psi>> psi);
}
