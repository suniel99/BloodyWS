/*
 * Really, really primitive DNA sequence aligner meant to demonstrate the
 * kinds of answers we want from your aligners.  This uses a brute-force
 * algorithm that can be greatly improved.
 *
 * Stephen A. Edwards, 2012
 *
 * Sample usage:
 *
 * ./align human_g1k_v37.bin ERR050082.filt.bin 100 0 9999
 *
 * The first argument is the reference genome in packed binary form.
 *
 * The second argument is the sequence file, also in packed binary form.
 *
 * The third argument is the number of base pairs per sequence
 *
 * The fourth (optional) argument is the starting sequence number
 *
 * The fifth (optional) argument is the ending sequence number
 *
 * If the ending sequence number is omitted, it is taken as the end of
 * the file.
 *
 * If the starting sequence number is omitted, it is taken as the
 * beginning of the file.
 */

#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <fcntl.h>
#include <errno.h>
#include <unistd.h>
#include <string.h>

/* Assumed alignment for sequences in the sequence file, in bytes */
#define SEQUENCE_ALIGN 8

/* Result of matching a single sequence; match() fills in an array */
struct match {
  off_t position;      /* Position of a match if count > 0 */
  unsigned int count;  /* Number of matches found */
};

/* For debugging: print a packed sequence */
void dump_sequence(char *bases, int bytes)
{
  static char base[] = {'A', 'C', 'G', 'T'};
  int i;
  for (i = 0 ; i != bytes; i++) {
      char c = bases[i];
      printf("%c%c%c%c",
	     base[c&0x3],
	     base[(c >> 2) & 0x3],
	     base[(c >> 4) & 0x3],
	     base[(c >> 6) & 0x3]);
    }
    printf("\n");
}

/* Attempt to match an array of two-bit sequences (*sequences)
 * against a reference sequence (*reference_base).
 *
 * Fill in the *matches array with the results.
 *
 * This allocates a buffer large enough to hold a single sequence
 * and shifts the whole reference sequence through it, one pair at a time.
 * At each point, this buffer is compared to each of the given sequences.
 */
void match(struct match *matches, /* Output array, one per sequence */
	   char *reference_base,  /* Beginning of reference sequence */
	   off_t reference_total, /* Number of bytes in the reference */
	   void *sequences,       /* Beginning of sequences */
	   int start_sequence,    /* Index of first sequence in array */
	   int sequence_length,   /* Base pairs per sequence */
	   int end_sequence)      /* One more than index of last sequence */
{
  /* Bytes per sequence, padded to be a multiple of SEQUENCE_ALIGN */
  int sequence_bytes = ((sequence_length + SEQUENCE_ALIGN * 4 - 1) & 
                        ~(SEQUENCE_ALIGN*4 - 1)) >> 2;
  char *reference = malloc(sequence_bytes); /* Buffer for reference sequence */
  void *sequence;              /* Pointer to sequence being compared         */
  int s;                       /* Current sequence number                    */
  off_t p,                     /* Current reference position (bases)         */
    last_p;                    /* Last reference position to consider (bases)*/
  int next_pair_shift;         /* Offset within last_rp to add next base     */
  unsigned long *rp,	       /* Pointer into reference buffer              */
    *last_rp,                  /* Last unsigned long in reference buffer     */
    shifted_in;                /* Bits to OR into next reference buffer word */
  int i;                       /* Miscellaneous loop index                   */

  if (reference == NULL) {
    fprintf(stderr, "Failed to allocate memory for the reference buffer\n");
    exit(1);
  }

  /* Determine a pointer to the last unsigned long in the reference buffer */
  last_rp = (unsigned long *)
    (reference + (((sequence_length + sizeof(unsigned long) * 4 - 1) &
		   ~(sizeof(unsigned long) * 4 - 1)) >> 2)) - 1;
     
  /* Prime the reference sequence buffer by copying bytes from the reference.
     Because sequence_bytes is a multiple of SEQUENCE_ALIGN, this usually
     copies more data than in a single short sequence; we truncate this below */
  memcpy(reference, reference_base, sequence_bytes);
 
  /* Truncate the higher-order bits in the last byte if the sequence length is
     not a multiple of 4 */
  if (sequence_length & 0x3)
    reference[sequence_length >> 2] &=
      (1 << ((sequence_length & 0x3) << 1)) - 1;

  /* Pad the rest of the reference buffer with 0's so it ends on a multiple
     of SEQUENCE_ALIGN bytes */
  for ( i = sequence_length >> 2 ; i & (SEQUENCE_ALIGN - 1) ; i++ )
     reference[i] = 0;

  /* Every time we shift the reference buffer, we'll OR a new base pair at
     its end, so compute where in an unsigned long we should put it. */
  next_pair_shift = ((sequence_length - 1) &
		     (4 * sizeof(unsigned long) - 1)) << 1;

  /* Main loop: step through the entire reference sequence one base pair at
     a time */

  last_p = (reference_total << 2) - sequence_length - 1;
  for (p = 0 ; p != last_p ; p++) {

    /* Inner loop: check each sequence.  s is the sequence number in the
       source file; sequence is the pointer into the sequence array. */
    for ( s = start_sequence, sequence = sequences ; s != end_sequence ;
	  s++, sequence += sequence_bytes )
      /* Compare the sequence to the reference buffer */
      if (memcmp(sequence, reference, sequence_bytes) == 0) {
	matches[s - start_sequence].count++;      /* Maintain count */
	matches[s - start_sequence].position = p; /* Record latest position */
      }

    /* Shift the reference sequence right two bits and shift in the next pair
     * from the reference.
     *
     * Work from the end of the reference buffer back to the beginning.
     * For each unsigned long, capture its two LSBs and move them to
     * the two MSBs, shift it right two bits (one base pair), then
     * OR in the shifted-out LSBs from the previous word.
     *
     * In the first iteration, OR in the next base pair from the
     * reference sequence: go to a byte in the reference sequence,
     * select one of the four base pairs it holds, shift it to the two
     * LSBs, mask it, then shift it left to its position in the
     * reference buffer
    */
    shifted_in = ((reference_base[(p + sequence_length) >> 2] >>
		   (((p + sequence_length) & 0x3) << 1)) & 0x3)
      << next_pair_shift;

    for (rp = last_rp ; rp != (unsigned long *) reference ; rp--) {
      unsigned long tmp = ((*rp) & 0x3) << (8 * sizeof(unsigned long) - 2);
      *rp = (*rp >> 2) | shifted_in;
      shifted_in = tmp;
    }
    *rp = (*rp >> 2) | shifted_in;

  }

  free(reference);
}

/*
 * Process command-line arguments, map reference and sequence data into
 * memory, call match(), and print the results
 */

int main(int argc, const char *argv[])
{
  const char *reference_filename, *sequence_filename;
  int reference_fd = -1, sequence_fd = -1;
  int sequence_length, sequence_bytes, start_sequence = -1, end_sequence = -1,
    num_sequences, i;
  off_t reference_total, sequence_total, sequence_window_offset;
  size_t sequence_window_length;

  long page_size;

  void *reference_base, *sequence_base, *sequences;

  struct stat file_status;

  struct match *matches;

  page_size = sysconf(_SC_PAGE_SIZE); /* needed for mmap */

  if (argc < 4) goto usage;

  reference_filename = argv[1];
  sequence_filename = argv[2];

  sequence_length = atoi(argv[3]);
  /* Pad the sequences out to a multiple of SEQUENCE_ALIGN bytes */
  sequence_bytes = ((sequence_length + SEQUENCE_ALIGN * 4 - 1) & 
		    ~(SEQUENCE_ALIGN * 4 - 1)) >> 2;

  if (sequence_length <= 0) {
    fprintf(stderr,
	    "Error: given sequence length must be an integer greater than zero\n");
    goto usage;
  }

  if ((reference_fd = open(reference_filename, O_RDONLY)) < 0) {
    fprintf(stderr, "Error opening reference file \"%s\": ",
	    reference_filename);
    perror((const char *) 0);
    goto usage;
  }

  if (fstat(reference_fd, &file_status)) {
    fprintf(stderr, "Error checking reference file \"%s\": ",
	    reference_filename);
    perror((const char *) 0);
    goto usage;
  }

  reference_total = file_status.st_size;

  if (reference_total < sequence_bytes) {
    fprintf(stderr, "Error: reference file is shorter than the given sequence length (%d)\n", sequence_length);
    goto usage;
  }

  if ((sequence_fd = open(sequence_filename, O_RDONLY)) < 0) {
    fprintf(stderr, "Error opening sequence file \"%s\": ", sequence_filename);
    perror((const char *) 0);
    goto usage;
  }

  if (fstat(sequence_fd, &file_status)) {
    fprintf(stderr, "Error checking sequence file \"%s\": ", sequence_filename);
    perror((const char *) 0);
    goto usage;
  }

  sequence_total = file_status.st_size;

  if (sequence_total < sequence_bytes) {
    fprintf(stderr, "Sequence file is too small\n");
    goto usage;
  }

  if (sequence_total % sequence_bytes != 0)
    fprintf(stderr, "Warning: sequence file may be truncated\n");

  num_sequences = sequence_total / sequence_bytes;

  if (argc > 4) start_sequence = atoi(argv[4]);
  if (start_sequence < 0) start_sequence = 0;
  if (start_sequence >= num_sequences) {
    fprintf(stderr, "Error: initial sequence number must be less than %d\n",
	    num_sequences);
    goto usage;
  }

  if (argc > 5) end_sequence = atoi(argv[5]);
  else end_sequence = num_sequences;
  if (end_sequence < start_sequence || end_sequence > num_sequences) {
    fprintf(stderr, "Error: End sequence number must be between %d and %d\n",
	    start_sequence, num_sequences);
    goto closeexit;
  }

  /* mmap the reference data */

  reference_base = mmap( (void *) 0, reference_total, PROT_READ, MAP_SHARED,
			 reference_fd, 0);

  if (reference_base == MAP_FAILED) {
    perror("Error when attempting to map the reference file");
    goto closeexit;
  }

  /* mmap the sequence data */

  /* compute the starting location by rounding down to the nearest
     page boundary; window length is the difference between this and the last
     page on which the sequences fall */
  sequence_window_offset = (start_sequence * sequence_bytes) & ~(page_size - 1);
  sequence_window_length = (((end_sequence * sequence_bytes) + (page_size - 1))
			    & ~(page_size - 1)) - sequence_window_offset;

  sequence_base = mmap( (void *) 0, sequence_window_length, PROT_READ,
			MAP_SHARED, sequence_fd, sequence_window_offset);

  if (sequence_base == MAP_FAILED) {
    perror("Error when attempting to map the sequence file");
    goto closeexit;
  }

  sequences = sequence_base +
    ((start_sequence * sequence_bytes) - sequence_window_offset);

  /* Allocate space to hold the results of matching */

  matches = malloc(sizeof(struct match) * (end_sequence - start_sequence));
  if (matches == NULL) {
    fprintf(stderr, "Failed to allocate memory for match information\n");
    goto unmap_sequences;
  }

  for ( i = 0 ; i < end_sequence - start_sequence ; i++ )
    matches[i].count = 0;

  /* Call the sequence matcher with the starting address of the reference,
     starting address of the sequences, the ending address, the number
     of pairs in the sequence, and the length of the reference */

  match(matches,
	reference_base, reference_total,
	sequences, start_sequence, sequence_length, end_sequence);

  /* Report all matches */
  for (i = start_sequence ; i != end_sequence ; ++i) {
    printf("%8d: ", i);
    if (matches[i - start_sequence].count) {
      printf("%9ld", matches[i - start_sequence].position);
      if (matches[i-start_sequence].count > 1)
	printf(" + %d others", matches[i-start_sequence].count - 1);
      printf("\n");
    } else
      printf("-\n");
  }

  free(matches);

 unmap_sequences:
  if (munmap(sequence_base, sequence_window_length)) {
    perror("Error when unmapping the sequence file");
    goto closeexit;
  }

 unmap_references:
  if (munmap(reference_base, reference_total)) {
    perror("Error when unmapping the reference file");
    goto closeexit;
  }

  close(sequence_fd);
  close(reference_fd);
  return 0;

 usage:
  fprintf(stderr,
	  "usage: align <reference-genome> <sequence-file> <sequence-length> <start> <end>\n"
	  "<reference-genome> is the name of a packed binary reference sequence.\n"
	  "<sequence-file> is the name of a packed binary sequence file.\n"
	  "<sequence-length> is an integer indicating the length, in base pairs, of each sequence.\n"
	  "<start> is the optional starting sequence number.  If omitted, it defaults to the start of the sequence file.\n"
	  "<end> is the optional ending sequence number.  If omitted, it defaults to the end of the sequence file.\n");
 closeexit:
  if (reference_fd >= 0) close(reference_fd);
  if (sequence_fd >= 0) close(sequence_fd);
  return 1;
}
