package meta;

/**
 * not: sltiu $result, $operand, 1
 * eql: xor $at, $operand1, $operand2
 * ~~~~ sltiu $result, $at, 1
 * JUMP: bne $cond, $0, target
 */
public enum Opr {
    add, sub, mult, div, mod,
    and, or, not,
    eql, neq, lt, gt,
    lw, sw, mov, cnst,
}
