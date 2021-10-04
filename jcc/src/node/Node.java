package node;

import meta.Meta;

public abstract class Node {
    public static Cursor cs;
    public Meta meta;

    public Node(NTyp typ) {
    }

    public abstract void forward();

    public abstract Meta compile();
}
