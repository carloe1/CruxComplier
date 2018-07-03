.data                         # BEGIN Data Segment
cruxdata.a: .space 4
data.newline:      .asciiz       "\n"
data.floatquery:   .asciiz       "float?"
data.intquery:     .asciiz       "int?"
data.trueString:   .asciiz       "true"
data.falseString:  .asciiz       "false"
                              # END Data Segment
.text                         # BEGIN Code Segment
func.printBool:
	lw $a0, 0($sp)
	beqz $a0, label.printBool.loadFalse
	la $a0, data.trueString
	j label.printBool.join
	label.printBool.loadFalse:
	la $a0, data.falseString
	label.printBool.join:
	li   $v0, 4
	syscall
	jr $ra
func.printFloat:
	l.s  $f12, 0($sp)
	li   $v0,  2
	syscall
	jr $ra
func.printInt:
	lw   $a0, 0($sp)
	li   $v0, 1
	syscall
	jr $ra
func.println:
	la   $a0, data.newline
	li   $v0, 4
	syscall
	jr $ra
func.readFloat:
	la   $a0, data.floatquery
	li   $v0, 4
	syscall
	li   $v0, 6
	syscall
	mfc1 $v0, $f0
	jr $ra
func.readInt:
	la   $a0, data.intquery
	li   $v0, 4
	syscall
	li   $v0, 5
	syscall
	jr $ra
.text                         # BEGIN Crux Program
## Variable Declaration: a ##
main:
	subu $sp, $sp, 8
	sw $fp, 0($sp)
	sw $ra, 4($sp)
	addi $fp, $sp, 8
## Getting Address Of: a ##
	la $t0, cruxdata.a
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## LiteralInt:9 ##
	li $t0, 9
	subu $sp, $sp, 4
	sw $t0, 0($sp)
#assignment
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	sw $t0, 0($t1)
label.1:
## Getting Address Of: a ##
	la $t0, cruxdata.a
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($t0)
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## LiteralInt:0 ##
	li $t0, 0
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	sge $t0, $t0, $t1
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	beqz $t1, label.2
## Getting Address Of: a ##
	la $t0, cruxdata.a
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($t0)
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	jal func.printInt			 # Function Call
	addi $sp, $sp, 4
	jal func.println			 # Function Call
## Getting Address Of: a ##
	la $t0, cruxdata.a
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## Subtraction: ast.Dereference(10,13) - ast.LiteralInt(10,15)[1] ##
## Getting Address Of: a ##
	la $t0, cruxdata.a
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($t0)
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## LiteralInt:1 ##
	li $t0, 1
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	sub $t0, $t0, $t1
	subu $sp, $sp, 4
	sw $t0, 0($sp)
#assignment
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	sw $t0, 0($t1)
	j label.1
label.2:
## Subtraction: ast.LiteralInt(12,14)[0] - ast.LiteralInt(12,16)[5] ##
## LiteralInt:0 ##
	li $t0, 0
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## LiteralInt:5 ##
	li $t0, 5
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	sub $t0, $t0, $t1
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	jal func.printInt			 # Function Call
	addi $sp, $sp, 4
## Getting Address Of: a ##
	la $t0, cruxdata.a
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($t0)
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	jal func.printInt			 # Function Call
	addi $sp, $sp, 4
label.0:
	li    $v0, 10
	syscall
                              # END Code Segment
