import java.util.*;
import java.io.*;

/*
 * Generates N*num_combo datasets with noise in motif location/presence
 * Key parameters are configurable motif length, sequence length and sequence count
 */

public class DatasetCreator {
    public static void main(String[] args) {

        ConfigReader configReader = new ConfigReader("config.txt");
        int countPerCombo = configReader.getIntValue("COUNT_PER_COMBO");

        ArrayList<Sequence> sequences = new ArrayList<>();
        for (int index = 0; index < countPerCombo; index++) {

            for (int i = 0; i < Constants.SEQUENCE_COMBOS.length; i++) {
                // get parameters from sequence combos array
                int ml = Constants.SEQUENCE_COMBOS[i][0];
                int sl = Constants.SEQUENCE_COMBOS[i][1];
                int sc = Constants.SEQUENCE_COMBOS[i][2];

                // Initialize and create the sequence
                SequenceCreator sCreator = new SequenceCreator(ml, sl, sc);
                sCreator.createSequence(index);
            }
        }
        System.out.println("Finished creating datasets");
    }
}