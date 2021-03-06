package lex.node;

import lex.Node;
import meta.Meta;

public class NullNode extends Node {
    public NullNode() {
        autoDisplay = false;
    }

    @Override
    public boolean forward() {
        return false;
    }

    @Override
    public Meta translate() {
        return Meta.Nop;
    }
}
