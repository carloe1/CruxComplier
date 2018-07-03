.data                         # BEGIN Data Segment
cruxdata.x: .space 60
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
## ArrayDeclaration: x ##
main:
	subu $sp, $sp, 8
	sw $fp, 0($sp)
	sw $ra, 4($sp)
	addi $fp, $sp, 8
	subu $sp, $sp, 4
## Variable Declaration: canary_begin ##
## Variable Declaration: canary_end ##
## Variable Declaration: outer ##
## Variable Declaration: inner ##
## Getting Address Of: canary_begin ##
	addi $t0, $fp, -12
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## LiteralInt:0 ##
	li $t0, 0
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	sw $t0, 0($t1)				# Assignment
## Getting Address Of: canary_end ##
	addi $t0, $fp, -12
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## LiteralInt:0 ##
	li $t0, 0
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	sw $t0, 0($t1)				# Assignment
## Getting Address Of: outer ##
	addi $t0, $fp, -12
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## LiteralInt:4 ##
	li $t0, 4
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	sw $t0, 0($t1)				# Assignment
## Getting Address Of: inner ##
	addi $t0, $fp, -12
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## LiteralInt:2 ##
	li $t0, 2
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	sw $t0, 0($t1)				# Assignment
label.1:
## Getting Address Of: outer ##
	addi $t0, $fp, -12
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
	sgt $t0, $t0, $t1
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	beqz $t1, label.2
label.3:
## Getting Address Of: inner ##
	addi $t0, $fp, -12
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
	sgt $t0, $t0, $t1
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	beqz $t1, label.4
## Getting Address Of: outer ##
	addi $t0, $fp, -12
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($t0)
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## Getting Address Of: inner ##
	addi $t0, $fp, -12
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($t0)
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## Getting Address Of: x ##
	la $t0, cruxdata.x
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	li $t2, 4
	mul $t1, $t1, $t2
	add $t0, $t0, $t1
	subu $sp, $sp, 4
	sw $t1, 0($sp)
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	li $t2, 4
	mul $t1, $t1, $t2
	add $t0, $t0, $t1
	subu $sp, $sp, 4
	sw $t1, 0($sp)
## Addition: ast.Addition(17,44) + ast.LiteralInt(17,60)[99] ##
## Addition: ast.Multiplication(17,37) + ast.Multiplication(17,52) ##
## Getting Address Of: inner ##
	addi $t0, $fp, -12
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($t0)
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## LiteralInt:1000 ##
	li $t0, 1000
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	mul $t0, $t0, $t1
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## Getting Address Of: outer ##
	addi $t0, $fp, -12
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($t0)
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## LiteralInt:100 ##
	li $t0, 100
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	mul $t0, $t0, $t1
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	add $t0, $t0, $t1
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## LiteralInt:99 ##
	li $t0, 99
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	add $t0, $t0, $t1
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	sw $t0, 0($t1)				# Assignment
## Getting Address Of: inner ##
	addi $t0, $fp, -12
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## Subtraction: ast.Dereference(18,19) - ast.LiteralInt(18,27)[1] ##
## Getting Address Of: inner ##
	addi $t0, $fp, -12
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
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	sw $t0, 0($t1)				# Assignment
	j label.3
label.4:
## Getting Address Of: inner ##
	addi $t0, $fp, -12
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## LiteralInt:2 ##
	li $t0, 2
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	sw $t0, 0($t1)				# Assignment
## Getting Address Of: outer ##
	addi $t0, $fp, -12
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## Subtraction: ast.Dereference(21,17) - ast.LiteralInt(21,25)[1] ##
## Getting Address Of: outer ##
	addi $t0, $fp, -12
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
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	sw $t0, 0($t1)				# Assignment
	j label.1
label.2:
## Getting Address Of: inner ##
	addi $t0, $fp, -12
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## LiteralInt:0 ##
	li $t0, 0
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	sw $t0, 0($t1)				# Assignment
## Getting Address Of: outer ##
	addi $t0, $fp, -12
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## LiteralInt:0 ##
	li $t0, 0
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	sw $t0, 0($t1)				# Assignment
label.5:
## Getting Address Of: outer ##
	addi $t0, $fp, -12
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($t0)
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
	slt $t0, $t0, $t1
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	beqz $t1, label.6
label.7:
## Getting Address Of: inner ##
	addi $t0, $fp, -12
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($t0)
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## LiteralInt:3 ##
	li $t0, 3
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	slt $t0, $t0, $t1
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	beqz $t1, label.8
## Getting Address Of: outer ##
	addi $t0, $fp, -12
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($t0)
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## Getting Address Of: inner ##
	addi $t0, $fp, -12
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($t0)
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## Getting Address Of: x ##
	la $t0, cruxdata.x
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	li $t2, 4
	mul $t1, $t1, $t2
	add $t0, $t0, $t1
	subu $sp, $sp, 4
	sw $t1, 0($sp)
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	li $t2, 4
	mul $t1, $t1, $t2
	add $t0, $t0, $t1
	subu $sp, $sp, 4
	sw $t1, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($t0)
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	jal func.printInt			 # Function Call
	addi $sp, $sp, 4
## Getting Address Of: inner ##
	addi $t0, $fp, -12
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## Addition: ast.Dereference(29,20) + ast.LiteralInt(29,28)[1] ##
## Getting Address Of: inner ##
	addi $t0, $fp, -12
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
	add $t0, $t0, $t1
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	sw $t0, 0($t1)				# Assignment
	j label.7
label.8:
	jal func.println			 # Function Call
## Getting Address Of: inner ##
	addi $t0, $fp, -12
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## LiteralInt:0 ##
	li $t0, 0
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	sw $t0, 0($t1)				# Assignment
## Getting Address Of: outer ##
	addi $t0, $fp, -12
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## Addition: ast.Dereference(33,18) + ast.LiteralInt(33,26)[1] ##
## Getting Address Of: outer ##
	addi $t0, $fp, -12
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
	add $t0, $t0, $t1
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	sw $t0, 0($t1)				# Assignment
	j label.5
label.6:
## Getting Address Of: canary_begin ##
	addi $t0, $fp, -12
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($t0)
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	jal func.printInt			 # Function Call
	addi $sp, $sp, 4
## Getting Address Of: canary_end ##
	addi $t0, $fp, -12
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
	addu $sp, $sp 4
	li    $v0, 10
	syscall
                              # END Code Segment
