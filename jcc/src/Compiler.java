import engine.Dojo;
import engine.seg.Data;
import engine.seg.Text;
import meta.Err;
import word.Word;

public class Compiler {
    public static void main(String[] args) {
        File file = new File("testfile.txt", "mips.txt").init();
        Lang lang = new Lang(new Word(file.read()), new Err(file.read()));
//        System.out.println(lang);
        Dojo.translate();
        System.out.println(lang);
        System.out.println(Dojo.toStr());
        file.write(new Data().toString());
        file.write(new Text().toString());
        file.close();
    }
}
