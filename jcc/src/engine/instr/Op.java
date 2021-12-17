package engine.instr;

public enum Op {
    addu, subu, mult, div, mfhi, mflo, mthi, mtlo, madd, msub,
    slt, sltu, sltiu, sll, sra, srl,
    beq, bne, bgez, blez, bgtz, bltz, j, jr, jal,
    or, xor, and, ori, li, lw, sw,
    addi, subi, andi, move, nop
}
