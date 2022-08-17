	.data
n:
	10
	.text
main:
	load %x0, $n, %x4
	sub %x5, %x5, %x5
	addi %x5, 1, %x6
	sub %x7, %x7, %x7
	addi %x7, 65535, %x7
	store %x5, $n, %x7
	subi %x7, 1, %x7
	addi %x4, 0, %x8
	sub %x9, %x9, %x9
	addi %x9, 1, %x9
	jmp forloop
forloop:
	bgt %x8, %x9, fib
	jmp exit
fib:
	store %x6, $n, %x7
	subi %x7, 1, %x7
	add %x5, %x6, %x10
	addi %x6, 0, %x5
	addi %x10, 0, %x6
	subi %x8, 1, %x8
	jmp forloop
exit:
	end