        .data
a:
        5
        .text
main:
        load %x0, $a, %x3
        sub %x7, %x7, %x7
loop:
        sub %x5, %x5, %x5
        subi %x3, 1, %x3
        addi %x9, 1, %x9
        addi %x9, 1, %x9
        addi %x9, 1, %x9
        beq %x5, %x3, palindrome
        addi %x6, 1, %x6
        addi %x6, 1, %x6
        addi %x6, 1, %x6
        jmp loop
        add %x10, 1, %x10
        add %x10, 1, %x10
palindrome:
        end
