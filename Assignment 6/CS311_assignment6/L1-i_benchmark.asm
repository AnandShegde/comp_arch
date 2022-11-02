.data
a:
    10
    .text
main:
    sub %x6, %x6, %x6
    addi %x6, 10, %x6
loop:
    addi %x9, 1, %x9
    addi %x9, 2, %x9
    addi %x9, 3, %x9
    addi %x9, 4, %x9
    addi %x9, 5, %x9
    addi %x9, 6, %x9
    subi %x6, 1, %x6
    bgt %x6, %x0, loop
    end