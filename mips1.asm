.data 0x10008000
base: .space 3
arr: .word 1 
s1: .asciiz "abcd"
s2: .asciiz "edfg"
.text
li $s1, -1
sw $s1, 0x0($gp)
li $v0, 10
la $s0, base
syscall
