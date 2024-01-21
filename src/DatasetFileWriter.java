import java.util.*;
import java.io.*;

/**
 * Utility Class to write dataset files
 * Used by DatasetCreator and DatasetCreatorWithNoise
 */

public class DatasetFileWriter {
    public String datasetFilenamePfx;

    // Constructor
    public DatasetFileWriter(int ml, int sl, int sc, int index) {
        this.datasetFilenamePfx = "dataset/dataset";
        this.datasetFilenamePfx += "_ml_" + ml;
        this.datasetFilenamePfx += "_sl_" + sl;
        this.datasetFilenamePfx += "_sc_" + sc;
        this.datasetFilenamePfx += "_index_" + (index + 1) + "_";
    }

    // Write Sequence in FASTA file format
    public void writeSequenceFile(ArrayList<Sequence> sequences) {
        String filename = datasetFilenamePfx + "sequences.fa";
        try {
            FileWriter filw = new FileWriter(filename);
            BufferedWriter bufw = new BufferedWriter(filw);
            for (int i = 0; i < sequences.size(); i++) {
                bufw.write(">Sequence " + (i + 1) + "\n");
                Sequence mySequence = sequences.get(i);
                bufw.write(mySequence.seq);
                bufw.write("\n");
            }
            bufw.close();
        } catch (IOException except) {
            System.out.println("IOException in writeSequenceFile");
        }
    }

    // Generic File Writer
    public void writeGenericFile(String data, String fileSfx) {
        String filename = datasetFilenamePfx + fileSfx;
        try {
            FileWriter filw = new FileWriter(filename);
            BufferedWriter bufw = new BufferedWriter(filw);
            bufw.write(data);
            bufw.close();
        } catch (IOException except) {
            System.out.println("IOException in writeGenericFile");
        }
    }

    // Motif Sites File Writer
    public void writeSitesFile(ArrayList<Sequence> sequences) {
        String filename = datasetFilenamePfx + "sites.txt";
        try {
            FileWriter filw = new FileWriter(filename);
            BufferedWriter bufw = new BufferedWriter(filw);
            for (int i = 0; i < sequences.size(); i++) {
                Sequence sequence = sequences.get(i);
                String location = sequence.motifLocation + "\n";
                bufw.write(location);
            }
            bufw.close();
        } catch (IOException except) {
            System.out.println("IOException in writeSitesFile");
        }
    }

}
