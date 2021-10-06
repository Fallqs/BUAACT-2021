
public class Compiler {
    public static void main(String[] args) {
        File file = new File("testfile.txt", "output.txt").init();
        Lang lang = new Lang(new Word(file.read()));
        file.write(lang.toString());
        file.close();
    }
}
