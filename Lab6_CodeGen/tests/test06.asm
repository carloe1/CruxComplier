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
cruxfunc.myPrintZero:
#Prologue
	subu $sp, $sp, 8
	sw $fp, 0($sp)
	sw $ra, 4($sp)
	addi $fp, $sp, 8
	li $t0, 0				# adding LiterlInt
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	jal func.printInt			 # Function Call
	addi $sp, $sp, 4
label.0:
#Epilogue
	lw $ra, 4($sp)
	lw $fp, 0($sp)
	addu $sp, $sp, 8
	jr $ra
cruxfunc.myPrintOne:
#Prologue
	subu $sp, $sp, 8
	sw $fp, 0($sp)
	sw $ra, 4($sp)
	addi $fp, $sp, 8
	addi $t0, $fp, 0
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($t0)
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	jal func.printInt			 # Function Call
	addi $sp, $sp, 4
label.1:
#Epilogue
	lw $ra, 4($sp)
	lw $fp, 0($sp)
	addu $sp, $sp, 8
	jr $ra
cruxfunc.myPrintTwo:
#Prologue
	subu $sp, $sp, 8
	sw $fp, 0($sp)
	sw $ra, 4($sp)
	addi $fp, $sp, 8
	addi $t0, $fp, 4
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($t0)
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	jal func.printInt			 # Function Call
	addi $sp, $sp, 4
	addi $t0, $fp, 0
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($t0)
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	jal func.printInt			 # Function Call
	addi $sp, $sp, 4
label.2:
#Epilogue
	lw $ra, 4($sp)
	lw $fp, 0($sp)
	addu $sp, $sp, 8
	jr $ra
cruxfunc.myPrintThree:
#Prologue
	subu $sp, $sp, 8
	sw $fp, 0($sp)
	sw $ra, 4($sp)
	addi $fp, $sp, 8
	addi $t0, $fp, 8
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($t0)
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	jal func.printInt			 # Function Call
	addi $sp, $sp, 4
	addi $t0, $fp, 4
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($t0)
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	jal func.printInt			 # Function Call
	addi $sp, $sp, 4
	addi $t0, $fp, 0
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	lw $t0, 0($sp)
	addiu $sp, $sp, 4
	lw $t0, 0($t0)
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	jal func.printInt			 # Function Call
	addi $sp, $sp, 4
label.3:
#Epilogue
	lw $ra, 4($sp)
	lw $fp, 0($sp)
	addu $sp, $sp, 8
	jr $ra
main:
#Prologue
	subu $sp, $sp, 8
	sw $fp, 0($sp)
	sw $ra, 4($sp)
	addi $fp, $sp, 8
	jal cruxfunc.myPrintZero			 # Function Call
	jal func.println			 # Function Call
	li $t0, 1				# adding LiterlInt
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	jal cruxfunc.myPrintOne			 # Function Call
	addi $sp, $sp, 4
	jal func.println			 # Function Call
	li $t0, 1				# adding LiterlInt
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	li $t0, 2				# adding LiterlInt
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	jal cruxfunc.myPrintTwo			 # Function Call
	addi $sp, $sp, 8
	jal func.println			 # Function Call
	li $t0, 1				# adding LiterlInt
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	li $t0, 2				# adding LiterlInt
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	li $t0, 3				# adding LiterlInt
	subu $sp, $sp, 4
	sw $t0, 0($sp)
	jal cruxfunc.myPrintThree			 # Function Call
	addi $sp, $sp, 12
	jal func.println			 # Function Call
label.4:
	li    $v0, 10
	syscall
                              # END Code Segment
