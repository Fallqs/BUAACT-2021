public class Compiler {
    public static void main(String[] args) {
        File file = new File("testfile.txt", "output.txt");
        Word word = new Word(file.read());
        file.write(word.toString());
        file.close();
    }
}
