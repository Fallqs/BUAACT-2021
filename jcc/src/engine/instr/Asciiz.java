package engine.instr;

public class Asciiz extends Instr {
    private final String name, cont;

    public Asciiz(String name, String cont) {
        this.name = name;
        this.cont = cont;
    }

    @Override
    public String toString() {
        return name + ": .asciiz " + '\"' + cont + '\"';
    }
}
