import java.util.*;
import java.io.*;

/**
 * This class creates ATCG DNA sequences through a stochastic process
 * motifs are inserted at random locations
 * 2 Forms of Noise are introduced based on input probabilities
 * - probErrorFree: 1-prob that a motif may have 1 char error
 * - probPresent: prob that a motif is present in a DNA sequence
 * <p>
 * Additionally, the sequences and meta information is stored in relevant files
 */

public class SequenceCreatorWithNoise {
    public int ml; // Motif Length
    public int sl; // Sequence Length
    public int sc; // Sequence Count

    // Constructor
    public SequenceCreatorWithNoise(int ml, int sl, int sc) {
        this.ml = ml;
        this.sl = sl;
        this.sc = sc;
    }

    // Generate Stochastic ATCG character sequence
    public String genStochasticString(int length) {
        Random rand = new Random();
        String options = "ACGT";
        String code = "";

        for (int i = 0; i < length; i++) {
            int randomNumber = rand.nextInt(4);
            code = code + options.charAt(randomNumber);
        }
        return code;
    }

    // Top level for sequence creation
    public void createSequence(
            int index,
            double probPresent,
            double probErrorFree) {
        ArrayList<Sequence> sequences = new ArrayList<>();
        String motif = genStochasticString(ml);
        String motifLengthStr = ml + "";
        sequences = sequencesOutput(sc, sl, motif, probPresent, probErrorFree);

        // Create output files
        DatasetFileWriter dfw = new DatasetFileWriter(ml, sl, sc, index);
        dfw.writeSequenceFile(sequences);
        dfw.writeGenericFile(motif, "motif.txt");
        dfw.writeGenericFile(motifLengthStr, "motifLength.txt");
        dfw.writeSitesFile(sequences);
    }

    // Sequence and motif creation
    public ArrayList<Sequence> sequencesOutput(
            int count,
            int length,
            String motif,
            double probPresent,
            double probErrorFree) {
        ArrayList<Sequence> sequences = new ArrayList<>();
        String code = "";
        for (int j = 0; j < count; j++) {
            code = genStochasticString(length);

            // Initialize and populate sequence
            Sequence mySequence = new Sequence();
            mySequence.motif = motif;
            mySequence.length = length;
            mySequence.seq = code;
            mySequence.hasMotif = false;
            mySequence.motifLength = motif.length();

            // Insert the motif
            modifySequence(mySequence, motif, probPresent, probErrorFree);
            sequences.add(mySequence);
        }
        return sequences;
    }

    // Insert motif in sequence and probabilistically add errors
    public int modifySequence(
            Sequence mySequence,
            String motif,
            double probPresent,
            double probErrorFree) {
        Random rand = new Random();
        int randomNumber = rand.nextInt(mySequence.seq.length() - motif.length());
        int motifChanged = 0;
        double randErrorFree = Math.random();
        if (randErrorFree > probErrorFree) {
            // introduce 1 char error
            int randLoc = rand.nextInt(motif.length());
            int randomBase = rand.nextInt(4);
            char newChar = 'A';
            if (randomBase == 0) newChar = 'A';
            if (randomBase == 1) newChar = 'C';
            if (randomBase == 2) newChar = 'G';
            if (randomBase == 3) newChar = 'T';
            motif = motif.substring(0, randLoc) + newChar + motif.substring(randLoc + 1, motif.length());
        }

        double randPresent = Math.random();
        if (randPresent < probPresent) {
            String trailingSegment = mySequence.seq.substring(randomNumber + motif.length(), mySequence.seq.length());
            mySequence.seq = mySequence.seq.substring(0, randomNumber) + motif + trailingSegment;
            mySequence.motifLocation = randomNumber;
        } else {
            mySequence.motifLocation = -1;

        }
        return motifChanged;
    }

}