package engine.instr;

public enum Op {
    addu, subu, mult, div, mfhi, mflo, beq, bne,
    slt, sltu, sltiu,
    bgez, blez, bgtz, bltz, lw, sw, j, jr, jal,
    or, xor, and, ori, li,
    addi, subi, sll, move, nop
}
