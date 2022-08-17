	.data
a:
	70
	80
	40
	20
	10
	30
	50
	60
n:
	8
	.text
main:
	load %x0, $n, %x4
	subi %x4, 1, %x9
	sub %x5, %x5, %x5
	jmp iloop
iloop:
	addi %x5, 1, %x6
	jmp jloop
	end
jloop:
	bgt %x6, %x9, compare
	load %x5, $a, %x7
	load %x6, $a, %x8
	blt %x7, %x8, swap
	addi %x6, 1, %x6
	jmp jloop
swap:
    store %x7, $a, %x6
	store %x8, $a, %x5
	addi %x6, 1, %x6
	jmp jloop
compare:
	addi %x5, 1, %x5
	blt %x5, %x4, iloop
	end