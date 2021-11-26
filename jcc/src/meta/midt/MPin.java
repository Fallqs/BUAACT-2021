package meta.midt;

import engine.sync.SyncR;

public class MPin {
    public String pin;
    public SyncR req;

    public MPin(String s) {
        pin = s;
    }

    @Override
    public String toString() {
        return pin;
    }
}
