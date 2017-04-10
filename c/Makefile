CC = gcc
CFLAGS = -O2 -Wall
LFLAGS = -lm
FILE = strassen

all:    $(FILE)

$(FILE):	$(FILE).o
	$(CC) $(LFLAGS) -o $(FILE) $(FILE).o

$(FILE).o:	$(FILE).c
	$(CC) $(CFLAGS) -c $(FILE).c
	
clean:
	rm -f *.o *~ $(FILE)
