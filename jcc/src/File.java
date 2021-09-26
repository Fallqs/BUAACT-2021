import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Character.isWhitespace;

public class File {
    private BufferedReader cin;
    private BufferedWriter cout;
    private ArrayList<Grid> input;

    public File(String i, String o) {
        try {
            cin = new BufferedReader(new FileReader(i));
            cout = new BufferedWriter(new FileWriter(o));
        } catch (IOException e) {
            e.printStackTrace();
        }
        init();
    }

    public ArrayList<Grid> read() {
        return input;
    }

    /*
     * \ensures input == file + '\n'
     * \ensures A == grids
     * \ensures B == empty
     */
    private void init() {
        ArrayList<Grid> v = new ArrayList<>();
        v.add(new Grid('\n'));
        try {
            String str;
            while ((str = cin.readLine()) != null) {
                for (char i : str.toCharArray()) v.add(new Grid(i));
                v.add(new Grid('\n'));
            }
            cin.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            v.add(new Grid('\n'));
        }
        input = del1(v);
    }

    /*
     * \ensures \result == filter(u[0:-1]) + '\n'
     */
    private ArrayList<Grid> del1(ArrayList<Grid> u) {
        ArrayList<Grid> v = new ArrayList<>();
        v.add(u.get(0));
        boolean bar = false, lin = false, format = false;
        for (int i = 1; i + 1 < u.size(); ++i) {
            if (u.get(i).c == '"' && !bar && !lin) format = !format;
            else if (u.get(i).c == '\n' && lin) lin = false;
            else if (u.get(i).c == '/' && u.get(i + 1).c == '/' && !bar && !format && !lin) lin = true;
            else if (u.get(i).c == '/' && u.get(i + 1).c == '*' && !bar && !format && !lin) {
                bar = true;
                ++i;
            } else if (u.get(i).c == '/' && u.get(i - 1).c == '*' && bar) {
                bar = false;
                u.get(i).c = ' ';
            }
            if (!bar && !lin) v.add(u.get(i));
        }
        v.add(u.get(u.size() - 1));
        for (Grid g : v) if (isWhitespace(g.c)) g.c = ' ';
        // System.out.println("del1::");
        // System.out.println(String.valueOf(v));
        return v;
    }

    /* \ensures \result == ' ' + filter(u) + ' ' */
    private ArrayList<Grid> del2(ArrayList<Grid> u) {
        ArrayList<Grid> v = new ArrayList<>();
        v.add(u.get(0));

        boolean on = false;
        boolean format = false;
        for (int i = 1; i + 1 < u.size(); ++i) {
            if (u.get(i).c == '"') format = !format;
            else if (u.get(i).c == '/' && u.get(i + 1).c == '/' && !on && !format) on = true;
            else if (u.get(i).c == '\n' && on) {
                on = false;
                u.get(i).c = ' ';
            }
            if (!on) v.add(u.get(i));
        }
        v.add(u.get(u.size() - 1));
        for (Grid g : v) if (isWhitespace(g.c)) g.c = ' ';
        // System.out.println("del2::");
        // System.out.println(String.valueOf(v));
        return v;
    }

    public void write(String s) {
        try {
            cout.write(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            cout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
