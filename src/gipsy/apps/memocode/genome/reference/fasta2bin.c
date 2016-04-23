/*
 * Convert a FASTA file (e.g., the human reference genome from the 1000 Genomes)
 * to a packed (but uncompressed) binary format.
 *
 * Stephen A. Edwards, 2012
 *
 * See http://en.wikipedia.org/wiki/FASTA_format
 *
 * Each byte holds four base pairs coded as two bits each:
 *
 * A  00
 * C  01
 * G  10
 * T  11
 *
 * Typical usage:
 *
 * gunzip -c human_g1k_v37.fasta.gz | fasta2bin > human_g1k_v37.bin
 */

#include <stdio.h>

#define MAXLINE 1024

int main()
{
  int c, i;
  /* Read and discard the header line */

  if (getchar() != '>') {
    fprintf(stderr, "Error: header line does not start with \">\"\n");
    return 1;
  }

  for (i = 0 ; ; i++) {
    c = getchar();
    if (c == '\n' || c == '\r') break;
    if (c == EOF) {
      fprintf(stderr, "Error: file ended before header line\n");
      return 1;
    }
    if (i == MAXLINE) {
      fprintf(stderr, "Error: header line is too long\n");
      return 1;
    }    
  }

  c = 0;
  i = 0;

  for (;;) {
    switch (getchar()) {
    case 'A': break;
    case 'C': c |= (1 << i); break;
    case 'G': c |= (2 << i); break;
    case 'T': case 'U': c |= (3 << i); break;

    case 'N': case 'K': case 'S': case 'Y':
    case 'M': case 'W': case 'R': case 'B':
    case 'D': case 'H': case 'V': case '-':
      break;

    case '\n': case '\r':
      continue;

    case EOF:
      if (i) putchar(c); /* Flush any last bits */
      return 0;
    }

    i += 2;
    if (i == 8) putchar(c), c = i = 0;
  }
}
