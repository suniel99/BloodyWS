CONTEST = 2012-memocode-contest

# Uncomment if you are in the Americas (the US, etc.)
FTP = ftp://ftp-trace.ncbi.nih.gov/1000genomes/ftp
# Uncomment if you are outside the Americas (Europe, etc.)
# FTP = ftp://ftp.1000genomes.ebi.ac.uk/vol1/ftp/

check : binaries short_reference.bin short_sequences.bin short-golden.txt
	./align short_reference.bin short_sequences.bin 100 > short.txt
	if diff short.txt short-golden.txt ; \
	then echo "SUCCESS" ; \
	else echo "FAILED" && exit 1 ; \
	fi

# Warning : running this self-check involved downloading over 1 Gb of information
# followed by perhaps 10 minutes of running the aligner

long-check : binaries human_g1k_v37.bin ERR050082.filt.bin
	./align human_g1k_v37.bin ERR050082.filt.bin 100 0 10 > ERR050082.txt
	if diff ERR050082.txt ERR050082-golden.txt ; \
	then echo "SUCCESS" ; \
	else echo "FAILED" && exit 1 ; \
	fi

binaries : fasta2bin bin2acgt fastq2bin align

# The reference genome to which we will compare everything

REFERENCE_GENOME = human_g1k_v37.fasta.gz
REFERENCE_BINARY = human_g1k_v37.bin

# Short sequence reads for individual NA06985

NA06985_SEQUENCES_ROOT = $(FTP)/data/NA06985/sequence_read

NA06985_SEQUENCES = \
	ERR050082_1.filt.fastq.gz \
	ERR050082_2.filt.fastq.gz \
	ERR068361.filt.fastq.gz \
	ERR068361_1.filt.fastq.gz \
	ERR068361_2.filt.fastq.gz \
	SRR006021.filt.fastq.gz \
	SRR103685.filt.fastq.gz \
	SRR103686.filt.fastq.gz \
	SRR171676.filt.fastq.gz

# Known alignments for individual NA06985

NA06985_ALIGNMENTS_ROOT = $(FTP)/data/NA06985/alignment

NA06985_ALIGNMENTS = \
	NA06985.chrom11.ILLUMINA.bwa.CEU.low_coverage.20111114.bam \
	NA06985.chrom20.ILLUMINA.bwa.CEU.low_coverage.20111114.bam \
	NA06985.mapped.ILLUMINA.bwa.CEU.low_coverage.20111114.bam \
	NA06985.unmapped.ILLUMINA.bwa.CEU.low_coverage.20111114.bam

TARFILES = README Makefile align.c fasta2bin.c bin2acgt.c fastq2bin.c \
	short_reference.fasta.gz short_sequences.fastq.gz \
	short-golden.txt ERR050082-golden.txt

tar : $(CONTEST).tar.gz

$(CONTEST).tar.gz : $(TARFILES)
	cd .. ; tar zcf $(CONTEST)/$(CONTEST).tar.gz \
	$(TARFILES:%=$(CONTEST)/%)

# Download data from 1000 Genomes FTP site
# WARNING: this downloads 10s of Gbs and therefore takes time

downloads : $(REFERENCE_GENOME) ERR050082.filt.fastq.gz $(NA06985_SEQUENCES) $(NA06985_ALIGNMENTS)

$(REFERENCE_GENOME) :
	wget $(FTP)/technical/reference/$(REFERENCE_GENOME)

ERR050082.filt.fastq.gz :
	wget -c $(NA06985_SEQUENCES_ROOT)/ERR050082.filt.fastq.gz

$(NA06985_SEQUENCES) :
	wget -c $(NA06985_SEQUENCES:%=$(NA06985_SEQUENCES_ROOT)/%)

$(NA06985_ALIGNMENTS) :
	wget -c $(NA06985_ALIGNMENTS:%=$(NA06985_ALIGNMENTS_ROOT)/%)

# Utility programs (format converters)
fasta2bin : fasta2bin.c
	cc -O -o fasta2bin fasta2bin.c

bin2acgt : bin2acgt.c
	cc -O -o bin2acgt bin2acgt.c

fastq2bin : fastq2bin.c
	cc -O -o fastq2bin fastq2bin.c

# The primitive aligner

align align.so : align.c
	cc -O -o align align.c
	gcc -O -c -fPIC align.c -o align.o
	gcc -shared align.o -o libalign.so

# How to convert from reference genome and sequence files to packed binary

%.bin : %.fasta.gz
	gunzip -c $< | ./fasta2bin > $@

%.bin : %.fastq.gz
	gunzip -c $< | ./fastq2bin > $@

human_g1k_v37.bin : $(REFERENCE_GENOME)

ERR050082.filt.bin : ERR050082.filt.fastq.gz

.PHONY : clean
clean :
	rm -f fasta2bin bin2acgt fastq2bin align short_sequences.bin \
	short_reference.bin short.txt ERR050082.txt
