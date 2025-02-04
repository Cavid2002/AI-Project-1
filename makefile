
CC= gcc
# CFLAGS = -Wall -Wextra


main: main.c ucs.o
	$(CC) $? -o $@

ucs.o: ucs.c 
	$(CC) -c $? -o $@

.PHONY = run

run: main
	./main