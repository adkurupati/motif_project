/**
 * Program Constants
 */

public class Constants {

    // file name constants (these will not change regardless of algorithm)
    public static String DATASET_FOLDER = "dataset/";
    public static String SEQUENCES_FILE = "sequences.fa";
    public static String PRED_MOTIF_FILE = "predictedmotif.txt";
    public static String PRED_SITES_FILE = "predictedsites.txt";
    public static String MOTIF_FILE = "motif.txt";
    public static String SITES_FILE = "sites.txt";
    public static String MOTIF_LENGTH_FILE = "motifLength.txt";

    // TODO: move these to config.txt instead
    public static int[][] SEQUENCE_COMBOS = {
            // motif length, sequence length, sequence count
            {8, 500, 10},
            {7, 500, 10},
            {6, 500, 10},
            {8, 1000, 10},
            {8, 2000, 10},
            {8, 500, 5},
            {8, 500, 20}
    };

}

