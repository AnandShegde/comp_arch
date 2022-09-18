	.data
a:
	765
	.text
main:
	load %x23, $a, %x5
	load %x23, $a, %x4
	addi %x10, 1, %x10
	addi %x7, 1, %x7
writerev:
	divi %x5, 10, %x5
	store %x31, $a, %x7
	addi %x7, 1, %x7
	bgt %x5, %x11, writerev
	subi %x7, 1, %x7
checkrev:
	load %x7, $a, %x30
	divi %x4, 10, %x4
	bne %x30, %x31, notpalindrome
	subi %x7, 1, %x7
	bgt %x4, %x11, checkrev
	end
notpalindrome:
	subi %x10, 2, %x10
	end