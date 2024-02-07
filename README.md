# Motif Finder Project

Motifs are short, recurring patterns in DNA that are presumed to have a biological function. This project involves developing a "motif finding" program.

The project consists of two components:
- Dataset Generator Program (Including the ability to inject noise into motifs)
- Motif Finder Program (three different algorithm variants have been implemented)


## Dataset Generator Program:
There are two variants of the Dataset Generator. The first variant creates perfect motifs, while the second variant introduces noise into the motifs.
The core sequence files are generated in the FASTA format.

## Noise-Free Dataset Generator
The noise-free generator takes the following parameters to generate 700 datasets:
- ML: Motif Length, taking a value in [6, 7, 8]
- SL: Sequence Length, taking a value in [500, 1000, 2000]
- SC: Sequence Count, taking a value in [5, 10, 20]

Based on these parameters, the sequence generator produces sequences incorporating motifs at random locations in a stochastic fashion. The generator also stores ground truth files (in motif.txt and sites.txt).

## Noise Additive Dataset Generator
The noise additive generator incorporates two types of noise in a probabilistic fashion:
- Each random sequence will have a probability (p_present) of containing the motif. That is, not all sequences will have the motif planted in them.
- When planting a motif in a sequence, with some probability (p_char_error) a one-character error will be introduced. 


## Motif Finder Program
Three Motif Finders have been implemented:
- Simple brute force finder (inefficient but useful as a baseline comparison check)
- Hashmap search-based finder (faster than brute force)
- Noise adaptive hashmap search (leverages the hashmap search finder but incorporates the ability to handle one-character noise errors)
    
