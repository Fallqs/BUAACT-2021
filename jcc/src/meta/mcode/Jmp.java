package meta.mcode;

import engine.SyncR;
import meta.Meta;

public class Jmp extends Meta {
    public SyncR tar;

    public Jmp(SyncR tar) {
        this.tar = tar;
    }

    @Override
    public String toString() {
        return "Jmp " +  tar;
    }
}
