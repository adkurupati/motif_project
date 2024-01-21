import java.util.*;
import java.io.*;

/**
 * This class creates stochastic ATCG DNA sequences
 * Motifs are inserted at random locations
 */

public class SequenceCreator {
    public int ml; // Motif Length
    public int sl; // Sequence Length
    public int sc; // Sequence Count

    // Constructor
    public SequenceCreator(int ml, int sl, int sc) {
        this.ml = ml;
        this.sl = sl;
        this.sc = sc;
    }

    // Top level for sequence creation
    public void createSequence(int index) {
        ArrayList<Sequence> sequences = new ArrayList<>();
        String motif = genStochasticString(ml);
        String motifLengthStr = ml + "";
        sequences = sequencesOutput(sc, sl, motif);

        // Create output files
        DatasetFileWriter dfw = new DatasetFileWriter(ml, sl, sc, index);
        dfw.writeSequenceFile(sequences);
        dfw.writeGenericFile(motif, "motif.txt");
        dfw.writeGenericFile(motifLengthStr, "motifLength.txt");
        dfw.writeSitesFile(sequences);
    }

    // Sequence and motif creation
    public ArrayList<Sequence> sequencesOutput(int count, int length, String motif) {
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
            modifySequence(mySequence, motif);
            sequences.add(mySequence);
        }
        return sequences;
    }

    // Generate Stochastic ATCG character sequence
    public String genStochasticString(int length) {
        Random rand = new Random();
        String options = "ATCG";
        String code = "";

        for (int i = 0; i < length; i++) {
            int randomNumber = rand.nextInt(4);
            code = code + options.charAt(randomNumber);
        }
        return code;
    }

    // Modify and insert motif at a random location
    public void modifySequence(Sequence mySequence, String motif) {
        // motif insertion index
        Random rand = new Random();
        int motifStart = rand.nextInt(mySequence.seq.length() - motif.length());
        int motifEnd = motifStart + motif.length();

        String startSegment = mySequence.seq.substring(0, motifStart);
        String trailingSegment = mySequence.seq.substring(motifEnd, mySequence.seq.length());

        // Concatenate start, motif and end  
        mySequence.seq = startSegment + motif + trailingSegment;
        mySequence.motifLocation = motifStart;

    }
}
    
