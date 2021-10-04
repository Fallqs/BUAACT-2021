package grammar;

import meta.Cursor;
import meta.Meta;

import java.util.ArrayList;

public abstract class Node {
    public static Cursor cs;
    public static ArrayList<String> output = new ArrayList<>();
    public static boolean display = true;
    public Meta meta;

    public abstract Boolean forward();

    public abstract Meta compile();
}
