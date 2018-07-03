.data                         # BEGIN Data Segment
cruxdata.a: .space 12
cruxdata.b: .space 40
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
## ArrayDeclaration: a ##
## ArrayDeclaration: b ##
main:
	subu $sp, $sp, 8
	sw $fp, 0($sp)
	sw $ra, 4($sp)
	addi $fp, $sp, 8
## LiteralInt:5 ##
	li $t0, 5
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## Getting Address Of: b ##
	la $t0, cruxdata.b
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
## LiteralInt:2 ##
	li $t0, 2
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	sw $t0, 0($t1)				# Assignment
## LiteralInt:5 ##
	li $t0, 5
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## Getting Address Of: b ##
	la $t0, cruxdata.b
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
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($t0)
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## Getting Address Of: a ##
	la $t0, cruxdata.a
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
## LiteralInt:100 ##
	li $t0, 100
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	sw $t0, 0($t1)				# Assignment
## LiteralInt:2 ##
	li $t0, 2
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## Getting Address Of: a ##
	la $t0, cruxdata.a
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
