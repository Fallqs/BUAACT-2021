import engine.Dojo;
import meta.Err;

public class Compiler {
    public static void main(String[] args) {
        File file = new File("testfile.txt", "mcode.txt").init();
        Lang lang = new Lang(new Word(file.read()), new Err(file.read()));
        file.write(Dojo.toStr());
        file.close();
    }
}
