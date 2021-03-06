package lex;

import engine.sync.SyncO;
import meta.Cursor;
import meta.Idents;
import meta.Meta;
import meta.ident.Var;
import meta.midt.MPin;
import word.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public abstract class Node {
    public static Cursor cs;
    public static Idents idt;
    public static List<Compo> output = new ArrayList<>();
    public static final Stack<MPin> breakPin = new Stack<>();
    public static final Stack<MPin> continuePin = new Stack<>();
    public static final Stack<SyncO> breaks = new Stack<>();
    public static final Stack<SyncO> continues = new Stack<>();
    public boolean autoDisplay = true;
    public Meta meta;
    protected NTyp typ;
    protected Result key;
    public Node fa;

    public abstract boolean forward();

    public Meta translate() {
        return null;
    }

    public void logIdt() {
    }

    public void reward(Result k) {
        this.key = k;
    }

    public Var rets() {
        return new Var(0);
    }

    public void dump(NTyp typ) {
        output.add(new Compo(typ, cs.pos()));
    }

    public void dump(NTyp typ, int p) {
        output.add(new Compo(typ, p));
    }

    public void dedump() {
        if (output.size() != 0) output.remove(output.size() - 1);
    }

    public NTyp gettyp() {
        return typ;
    }

    public boolean fwd() {
        boolean ret = forward();
        if (autoDisplay && ret && typ != null) dump(this.typ);
        return ret;
    }
}
