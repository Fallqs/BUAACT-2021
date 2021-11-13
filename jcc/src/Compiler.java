import engine.Dojo;
import engine.seg.Data;
import meta.Err;
import word.Word;

public class Compiler {
    public static void main(String[] args) {
        File file = new File("testfile.txt", "mips.txt").init();
        Lang lang = new Lang(new Word(file.read()), new Err(file.read()));
        Dojo.sort();
        System.out.println(lang);
        System.out.println(Dojo.toStr());
        file.write(new Data().toString());
        file.close();
    }
}
