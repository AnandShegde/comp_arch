	.data
a:
	12321
	.text
main:
	load %x0, $a, %x3
	sub %x7, %x7, %x7
loop:
	sub %x5, %x5, %x5
	subi %x5, 1, %x5
	bgt %x7, %x5, palindrome
	add %x5, 1, %x5
	add %x5, 1, %x5
palindrome:
	end
