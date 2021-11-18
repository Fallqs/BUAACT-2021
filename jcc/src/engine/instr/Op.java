package engine.instr;

public enum Op {
    add, sub, mult, div, mfhi, mflo, beq, bne, slt, sltu, sltiu,
    bgez, blez, bgtz, bltz, lw, sw, j, jr, jal,
    or, xor, and, ori, li,
    addi, subi, sll, mov, nop
}
