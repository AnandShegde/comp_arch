	.data
a:
	10
	.text
main:
	load %x0, $a, %x4
	divi %x4, 2, %x5
	addi %x31, 0, %x10
	sub %x8, %x8, %x7
	beq %x10, %x7, success
	end
success:
	subi %x10, 1, %x10
	end