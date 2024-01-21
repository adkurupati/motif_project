import java.util.*;
import java.io.*;

/*
 * Motif Finder that can handle noise/errors in sequences
 * Single char errors are accounted for as well as missing motifs in sequences
 * The algorithm will perform poorly for more than 1 char errors
 * Builds on top of MotifFinderHmap, but enhanced to handle noise scenarios
 */

public class MotifFinderAdaptive {
    ConfigReader configReader;

    // Constructor
    public MotifFinderAdaptive(ConfigReader configReader) {
        this.configReader = configReader;
    }

    // Top Level Find Method that should be invoked outside the class
    public ArrayList<ResultHolder> find() {
        ArrayList<ResultHolder> rhs = new ArrayList<>();
        ArrayList<String> fileNames = MotifFinderUtils.getAllMotifFiles();

        for (int j = 0; j < fileNames.size(); j++) {
            String fileName = fileNames.get(j);

            long start = System.currentTimeMillis();
            adaptiveFindMotif(fileNames.get(j));
            long timeElapsed = System.currentTimeMillis() - start;

            ResultHolder rh = createResultHolder(fileName, timeElapsed);
            rhs.add(rh);
        }
        MotifFinderUtils.analyzeResults(rhs);
        return rhs;
    }

    /**
     * Implements a 2 pass scheme
     * The first pass attempts to find motifs that are exact matches
     * In the 2nd pass we look for 1 char errors across the sequences
     */
    public void adaptiveFindMotif(String filePfx) {
        String fileName = filePfx + Constants.MOTIF_LENGTH_FILE;
        int motifLength = MotifFinderUtils.motifLengthReader(fileName);

        fileName = filePfx + Constants.SEQUENCES_FILE;
        ArrayList<String> sequenceList = MotifFinderUtils.sequenceReader(fileName);

        ArrayList<HashMap<String, Integer>> motifHmaps =
                buildHashMaps(sequenceList, motifLength);

        int min_count = 2; // Motif needs to be in atleast 2 sequences
        int best_count = 0;
        String bestMotif = "";
        ArrayList<Integer> bestMotifLocation = new ArrayList<Integer>();
        ArrayList<Integer> motifLocation = new ArrayList<Integer>();
        ArrayList<Integer> candidate_sequence_indexes = new ArrayList<Integer>();

        for (String possibleM : motifHmaps.get(0).keySet()) {
            motifLocation = new ArrayList<Integer>();
            candidate_sequence_indexes = new ArrayList<Integer>();
            int count = firstPassSearch(
                    possibleM, motifHmaps,
                    candidate_sequence_indexes, motifLocation);

            if ((count > min_count) && (count > best_count)) {
                findAlteredCandidates(
                        possibleM, sequenceList,
                        candidate_sequence_indexes, motifLocation);
                bestMotif = possibleM;
                best_count = count;
                bestMotifLocation = new ArrayList<>(motifLocation);
            }
        }

        if (best_count < min_count) {
            bestMotif = "motifnotfound";
        }
        writePredictedResults(filePfx, bestMotif, bestMotifLocation);
    }

    // first pass: search using multiple Hashmaps
    public int firstPassSearch(
            String possibleM,
            ArrayList<HashMap<String, Integer>> motifHmaps,
            ArrayList<Integer> candidate_sequence_indexes,
            ArrayList<Integer> motifLocation) {
        int count = 0;
        for (int i = 0; i < motifHmaps.size(); i++) {
            if (motifHmaps.get(i).containsKey(possibleM)) {
                motifLocation.add(motifHmaps.get(i).get(possibleM));
                count++;
            } else {
                motifLocation.add(-1);
                candidate_sequence_indexes.add(i);
            }
        }
        return count;
    }

    // 2nd pass: find motifs that may have 1-char errors
    public void findAlteredCandidates(
            String possibleM,
            ArrayList<String> sequenceList,
            ArrayList<Integer> candidate_sequence_indexes,
            ArrayList<Integer> motifLocation) {

        for (int k = 0; k < candidate_sequence_indexes.size(); k++) {
            int candidate_index = candidate_sequence_indexes.get(k);
            String candidate_sequence = sequenceList.get(candidate_index);
            int location = -1;
            String bases = "ATCG";
            for (int j = 0; j < possibleM.length(); j++) {
                for (int l = 0; l < 4; l++) {
                    String pre = possibleM.substring(0, j);
                    String post = possibleM.substring(j + 1, possibleM.length());
                    String altered_motif = pre + bases.charAt(l) + post;
                    location = candidate_sequence.indexOf(altered_motif);
                    if (location > -1) {
                        break;
                    }
                }
                if (location > -1) {
                    break;
                }
            }
            motifLocation.set(candidate_index, location);
        }
    }

    // Build Hash Maps (one for each sequence in the sequence list)
    public ArrayList<HashMap<String, Integer>> buildHashMaps(
            ArrayList<String> sequenceList,
            int motifLength) {
        ArrayList<HashMap<String, Integer>> motifHmaps = new ArrayList<>();
        for (int i = 0; i < sequenceList.size(); i++) {
            String seq = sequenceList.get(i);
            int length = seq.length();
            HashMap<String, Integer> sequenceHash = new HashMap<>();
            for (int j = 0; j < length - motifLength; j++) {
                String possibleMotif = seq.substring(j, j + motifLength);
                sequenceHash.put(possibleMotif, j);
            }
            motifHmaps.add(sequenceHash);
        }
        return motifHmaps;
    }

    // Store Results Found
    public void writePredictedResults(
            String filePfx,
            String possibleMotif,
            ArrayList<Integer> motifLocation) {
        // Store Sites Found
        try {

            String fileName = Constants.DATASET_FOLDER + filePfx;
            fileName += Constants.PRED_SITES_FILE;
            FileWriter filw = new FileWriter(fileName);
            BufferedWriter bufw = new BufferedWriter(filw);
            for (int k = 0; k < motifLocation.size(); k++) {
                bufw.write(motifLocation.get(k) + "\n");
            }
            bufw.close();
        } catch (IOException except) {
            System.out.println("IOException in writing PredictedResults");
        }

        // Store Motif found
        try {
            String fileName = Constants.DATASET_FOLDER + filePfx;
            fileName += Constants.PRED_MOTIF_FILE;
            FileWriter filw = new FileWriter(fileName);
            BufferedWriter bufw = new BufferedWriter(filw);
            bufw.write(possibleMotif);
            bufw.close();
        } catch (IOException except) {
            System.out.println("IOException in writing PredictedResults");
        }
    }

    // Store Results in a ResultsHolder Object for future stats calculations
    public ResultHolder createResultHolder(String fileName, long timeElapsed) {
        int motifMatch = MotifFinderUtils.checkMotifMatch(fileName);
        int sitesMatch = MotifFinderUtils.checkSitesMatch(fileName);

        // get ml, sc, sc and index from filename
        int ml = MotifFinderUtils.getIntValueFromFilename(fileName, "_ml_");
        int sl = MotifFinderUtils.getIntValueFromFilename(fileName, "_sl_");
        int sc = MotifFinderUtils.getIntValueFromFilename(fileName, "_sc_");
        int index = MotifFinderUtils.getIntValueFromFilename(fileName, "_index_");

        ResultHolder rh = new ResultHolder(ml, sl, sc);
        rh.setIndex(index);
        rh.setTime(timeElapsed);
        rh.setMotifMatch(motifMatch);
        rh.setSitesMatch(sitesMatch);

        return rh;
    }

}
