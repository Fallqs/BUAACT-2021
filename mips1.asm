.text
li $t1, -15
li $t2, 1431655765
li $t3, 3
srl $at, $t1, 1
mtlo $at
li $t7, -1
mthi $t7
madd $t1, $t2
mfhi $s1
sra $s1, $s1, 0
div $t1, $t3
mflo $s2
