	.data
a:
	1
	2
	3
	4
	5
	6
	7
	.text
main:
	sub %x4, %x4, %x4
	sub %x3, %x3, %x3
	addi %x3, 10, %x3
for:
	load %x4, $a, %x4
	load %x4, $a, %x4
	load %x4, $a, %x4
	load %x4, $a, %x4
	load %x4, $a, %x4
	load %x4, $a, %x4
	load %x4, $a, %x4
	sub %x4, %x4, %x4
	subi %x3, 1, %x3
	bgt %x3, %x0, for
	end
