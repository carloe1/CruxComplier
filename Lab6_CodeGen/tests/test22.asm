.data                         # BEGIN Data Segment
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
cruxfunc.fib:
	subu $sp, $sp, 8
	sw $fp, 0($sp)
	sw $ra, 4($sp)
	addi $fp, $sp, 8
## Getting Address Of: a ##
	addi $t0, $fp, 0
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
	sle $t0, $t0, $t1
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	beqz $t1, label.1
## LiteralInt:1 ##
	li $t0, 1
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $v0, 0($sp)
	addiu $sp, $sp, 4
	j label.0
	j label.2
label.1:
label.2:
## Addition: ast.Call(5,10)[Symbol(fib:func(TypeList(int)):int)] + ast.Call(5,23)[Symbol(fib:func(TypeList(int)):int)] ##
## Subtraction: ast.Dereference(5,16) - ast.LiteralInt(5,18)[1] ##
## Getting Address Of: a ##
	addi $t0, $fp, 0
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
	jal cruxfunc.fib			 # Function Call
	addi $sp, $sp, 4
 	subu $sp, $sp, 4
	sw $v0, 0($sp)
## Subtraction: ast.Dereference(5,29) - ast.LiteralInt(5,31)[2] ##
## Getting Address Of: a ##
	addi $t0, $fp, 0
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($t0)
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## LiteralInt:2 ##
	li $t0, 2
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	sub $t0, $t0, $t1
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	jal cruxfunc.fib			 # Function Call
	addi $sp, $sp, 4
 	subu $sp, $sp, 4
	sw $v0, 0($sp)
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	add $t0, $t0, $t1
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $v0, 0($sp)
	addiu $sp, $sp, 4
	j label.0
label.0:
	lw $ra, 4($sp)
	lw $fp, 0($sp)
	addu $sp, $sp, 8
	jr $ra
main:
	subu $sp, $sp, 8
	sw $fp, 0($sp)
	sw $ra, 4($sp)
	addi $fp, $sp, 8
## LiteralInt:5 ##
	li $t0, 5
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	jal cruxfunc.fib			 # Function Call
	addi $sp, $sp, 4
 	subu $sp, $sp, 4
	sw $v0, 0($sp)
	jal func.printInt			 # Function Call
	addi $sp, $sp, 4
label.3:
	li    $v0, 10
	syscall
                              # END Code Segment
