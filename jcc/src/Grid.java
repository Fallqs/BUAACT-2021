public class Grid {
    public final int x, y;
    public char c;
    private static int line = 0, col = 1;

    public Grid(char c) {
        this.c = c;
        this.x = line;
        this.y = col++;
        if (c == '\n') {
            ++line;
            col = 1;
        }
    }

    public String toString() {
        return "(" + c + "," + x + "," + y + ")";
    }
}