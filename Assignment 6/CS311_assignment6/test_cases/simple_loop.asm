.data
a:
	10
	60
n:
	2
	.text
main:
	load %x0, $a, %x3
	addi %x4, 100, %x4
	store %x3, 0, %x4
	jmp loop
loop:
	subi %x3, 1, %x3
	bgt %x3, %x0, loop
	end