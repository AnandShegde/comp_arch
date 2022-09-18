	.data
a:
	30
	.text
main:
	load %x0, $a, %x4
	sub %x5, %x5, %x5
	sub %x7, %x7, %x7
	addi %x5, 2, %x5
	div %x4, %x5, %x6
	addi %x6, 2, %x6
	jmp forloop
forloop:
	blt %x5, %x6, isprime
	jmp prime
isprime:
	div %x4, %x5, %x8
	beq %x31, %x7, notprime
	addi %x5, 1, %x5
	jmp forloop
notprime:
	subi %x7, 1, %x10
	end
prime:
	addi %x7, 1, %x10
	end