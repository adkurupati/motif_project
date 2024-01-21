import java.util.*;
import java.io.*;

/*
 * Generates N*num_combo datasets
 * Additionally Noise is introduced in motifs
 * Key parameters are configurable motif length, sequence length and sequence count
 * Key noise parameters are probability present and probability error free
 */

public class DatasetCreatorWithNoise {
    public static void main(String[] args) {

        // get config values
        ConfigReader configReader = new ConfigReader("config.txt");
        int countPerCombo = configReader.getIntValue("COUNT_PER_COMBO");
        double probPresent = configReader.getDoubleValue("PROB_MOTIF_PRESENT");
        double probErrorFree = configReader.getDoubleValue("PROB_MOTIF_ERROR_FREE");

        ArrayList<Sequence> sequences = new ArrayList<>();
        for (int index = 0; index < countPerCombo; index++) {
            for (int i = 0; i < Constants.SEQUENCE_COMBOS.length; i++) {
                // get parameters from sequence combos array
                int ml = Constants.SEQUENCE_COMBOS[i][0];
                int sl = Constants.SEQUENCE_COMBOS[i][1];
                int sc = Constants.SEQUENCE_COMBOS[i][2];

                // Initialize and create the sequence 
                SequenceCreatorWithNoise sCreator = new SequenceCreatorWithNoise(ml, sl, sc);

                sCreator.createSequence(
                        index, probPresent, probErrorFree);
            }
        }
        System.out.println("Finished creating datasets");
    }
}
