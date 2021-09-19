import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static java.lang.Character.isWhitespace;

public class File {
    private BufferedReader cin;
    private BufferedWriter cout;

    public File(String i, String o){
        try {
            cin = new BufferedReader(new FileReader(i));
            cout = new BufferedWriter(new FileWriter(o));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    /*
     * \ensures \result == file + '\n'
     */
    public char[] read() {
        StringBuilder ret = new StringBuilder();
        ret.append(' ');
        char[] ans;
        try {
            String str;
            while ((str = cin.readLine()) != null) {
                ret.append(str);
                ret.append("\n");
            }
            cin.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ret.append('\n');
            ans = new char[ret.length()];
            ret.getChars(0, ret.length(), ans, 0);
        }
        return del2(del1(ans));
    }

    /*
     * \ensures \result == filter(u[0:-1]) + '\n'
     */
    private char[] del1(char[] u) {
        StringBuilder ret = new StringBuilder();
        ret.append(' ');
        boolean on = false;
        boolean format = false;
        for (int i = 1; i + 1 < u.length; ++i) {
            if (u[i] == '"') format = !format;
            else if (u[i] == '/' && u[i + 1] == '*' && !on && !format) {
                on = true;
                ++i;
            } else if (u[i] == '/' && u[i - 1] == '*' && on) {
                on = false;
                u[i] = ' ';
            }
            if (!on) ret.append(u[i]);
        }
        ret.append('\n');
        char[] v = new char[ret.length()];
        ret.getChars(0, ret.length(), v, 0);
        // System.out.println("del1::");
        // System.out.println(String.valueOf(v));
        return v;
    }

    /* \ensures \result == ' ' + filter(u) + ' ' */
    private char[] del2(char[] u) {
        StringBuilder ret = new StringBuilder();
        ret.append(' ');
        boolean on = false;
        boolean format = false;
        for (int i = 1; i + 1 < u.length; ++i) {
            if (u[i] == '"') format = !format;
            else if (u[i] == '/' && u[i + 1] == '/' && !on && !format) on = true;
            else if (u[i] == '\n' && on) {
                on = false;
                u[i] = ' ';
            }
            if (!on) ret.append(u[i]);
        }
        char[] v = new char[ret.length()];
        ret.getChars(0, ret.length(), v, 0);
        for (int i = 0; i < v.length; ++i) if (isWhitespace(v[i])) v[i] = ' ';
        // System.out.println("del2::");
        // System.out.println(String.valueOf(v));
        return v;
    }

    public void write(String s) {
        try{
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
