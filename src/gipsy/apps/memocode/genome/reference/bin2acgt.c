/*
 * Interpret the bits in a binary file as packed ATGC data
 *
 * Stephen A. Edwards, 2012
 *
 * This is close to the inverse of what "fasta2bin" does.
 *
 * The encoding:
 *
 * A  00
 * C  01
 * G  10
 * T  11
 *
 * Usage: bin2acgt <opt-num-bytes> < file.bin
 *
 * Where, if supplied, <opt-num-bytes> is a decimal count of the number of
 * groups of four bases (i.e., the number of bytes) to print per line.  If
 * omitted, no newlines are ever printed.
 */

#include <stdio.h>
#include <stdlib.h>

char base[] = {'A', 'C', 'G', 'T'};

int main(int argc, const char *argv[])
{
  int c, n, bytes = 0;
  if (argc > 1) {
    bytes = atoi(argv[1]);
  }

  n = bytes;
  for (;;) {
    c = getchar();
    if (c == EOF) break;
    printf("%c%c%c%c",
	   base[c&0x3],
	   base[(c >> 2) & 0x3],
	   base[(c >> 4) & 0x3],
	   base[(c >> 6) & 0x3]);
    if (--n == 0) {
      putchar('\n');
      n = bytes;
    }
  }

  return 0;
}
