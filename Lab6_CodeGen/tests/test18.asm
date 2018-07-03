.data                         # BEGIN Data Segment
cruxdata.canary_begin: .space 4
cruxdata.a: .space 12
cruxdata.canary_end: .space 4
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
## Variable Declaration: canary_begin ##
## ArrayDeclaration: a ##
## Variable Declaration: canary_end ##
main:
	subu $sp, $sp, 8
	sw $fp, 0($sp)
	sw $ra, 4($sp)
	addi $fp, $sp, 8
	subu $sp, $sp, 4
## Variable Declaration: counter ##
## Getting Address Of: canary_begin ##
	la $t0, cruxdata.canary_begin
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
	la $t0, cruxdata.canary_end
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
## LiteralInt:0 ##
	li $t0, 0
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
## LiteralFloat: 1.11 ##
	li.s $f0, 1.11
	subu $sp, $sp, 4
	swc1 $f0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	sw $t0, 0($t1)				# Assignment
## LiteralInt:1 ##
	li $t0, 1
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
## LiteralFloat: 2.22 ##
	li.s $f0, 2.22
	subu $sp, $sp, 4
	swc1 $f0, 0($sp)
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
## LiteralFloat: 3.33 ##
	li.s $f0, 3.33
	subu $sp, $sp, 4
	swc1 $f0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t1, 0($sp)
	addiu $sp, $sp, 4
	sw $t0, 0($t1)				# Assignment
## Getting Address Of: canary_begin ##
	la $t0, cruxdata.canary_begin
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
## Getting Address Of: counter ##
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
label.1:
## Getting Address Of: counter ##
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
	beqz $t1, label.2
## Getting Address Of: counter ##
	addi $t0, $fp, -12
	subu $sp, $sp, 4
	sw $t0, 0($sp)
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
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($t0)
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	jal func.printFloat			 # Function Call
	addi $sp, $sp, 4
	jal func.println			 # Function Call
## Getting Address Of: counter ##
	addi $t0, $fp, -12
	subu $sp, $sp, 4
	sw $t0, 0($sp)
## Addition: ast.Dereference(24,19) + ast.LiteralInt(24,29)[1] ##
## Getting Address Of: counter ##
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
	j label.1
label.2:
## Getting Address Of: canary_end ##
	la $t0, cruxdata.canary_end
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
