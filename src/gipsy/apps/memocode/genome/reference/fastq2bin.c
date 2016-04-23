/*
 * FASTQ-to-binary format converter
 *
 * Stephen A. Edwards, 2012
 *
 * This converts a file in FASTQ format into packed (but uncompressed)
 * binary with four base pairs per byte, LSB to MSB.  Each sequence's
 * data is eight-byte aligned -- padded with zeros as necessary.
 *
 * http://en.wikipedia.org/wiki/FASTQ_format
 *
 * Each read sequence is read and packed into bytes, one after the
 * other.  all other information is discarded.
 * 
 * This insists each read sequence is the same length so locating the
 * nth sequence in the output stream amounts to multiplying n by a
 * constant (1/4 of the length of the sequence)
 *
 * The encoding:
 *
 * A  00
 * C  01
 * G  10
 * T  11
 *
 * Example usage:
 *
 * gunzip -c ERR050082.filt.fastq.gz | ./fastq2bin > ERR050082.filt.bin
 */

#include <stdio.h>
#include <stdlib.h>

#define MAXLINE 1024

#define ALIGN 8

unsigned int line = 1;

int skipline()
{
  int i, c;

  for (i = 0 ; i < MAXLINE ; i++) {
    c = getchar();
    if (c == '\n' || c == '\r') {
      line++;
      return 1;
    }
    if (c == EOF) return 0;
  }
  fprintf(stderr, "Error:%d:line too long\n", line);
  exit(1);
}

int main()
{
  int c;
  unsigned char byte;
  int i, bytes, expected_bytes = 0;
  for (;;) {
    if ((c = getchar()) == EOF) break;
    if (c != '@') {
      fprintf(stderr,
	      "Error:%d:expected \"@\" at the beginning of a sequence read, not '%c'\n",
	      line, c);
      return 1;
    }
    if (!skipline()) {
      fprintf(stderr, "Error:%d:end-of-file in sequence identifier line\n", line);
      return 1;
    }

    byte = i = bytes = 0;
    for (;;) {
      switch (getchar()) {
      case 'A': break;
      case 'C': byte |= (1 << i); break;
      case 'G': byte |= (2 << i); break;
      case 'T': case 'U': byte |= (3 << i); break;

      case 'N': case 'K': case 'S': case 'Y':
      case 'M': case 'W': case 'R': case 'B':
      case 'D': case 'H': case 'V': case '-':
	break;
	
      case '\n': case '\r':
	line++;
	if (i) {
          putchar(byte);
	  bytes++;
        }
	if (expected_bytes && bytes != expected_bytes) {
	  fprintf(stderr, "Error:%d:inconsistent sequence length\n", line-1);
	  return 1;
	} else expected_bytes = bytes;
	/* pad */
	for ( ; bytes & (ALIGN - 1) ; bytes++) putchar(0);
	goto nextline;
	
      case EOF:
	fprintf(stderr, "Error:%d:unexpected end-of-file in a sequence\n", line);
	return 1;
      }

      i += 2;
      if (i == 8) {
	putchar(byte);
        bytes++;
	byte = i = 0;
      }
    }
  nextline:

    if ((c = getchar()) == EOF) break;
    if (c != '+') {
      fprintf(stderr, "Error:%d:expected \"+\"\n", line);
      return 1;
    }

    if (!skipline()) {
      fprintf(stderr, "Error:%d: end-of-file in + line\n", line);
      return 1;
    }

    if (!skipline()) {
      fprintf(stderr,
	      "Error:%d:end-of-file in quality line\n", line);
      return 1;
    }
   
  }

  return 0;
}
