package node;

import java.util.List;

public class Cursor {
    public Word.Result[] token;
    public int p = 0;
    public Cursor(List<Word.Result> soup) {
        token = soup.toArray(new Word.Result[0]);
    }
}
