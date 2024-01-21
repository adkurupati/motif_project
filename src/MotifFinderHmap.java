import java.util.*;
import java.io.*;

/*
 * Fast Motif Finder, but assumes no noise in motifs
 * Employs multiple HashMaps to facilitate a fast search
 */

public class MotifFinderHmap {
    ConfigReader configReader;

    public MotifFinderHmap(ConfigReader configReader) {
        this.configReader = configReader;
    }

    // Top Level Find Method that should be invoked outside the class
    public ArrayList<ResultHolder> find() {
        ArrayList<ResultHolder> rhs = new ArrayList<>();
        ArrayList<String> fileNames = MotifFinderUtils.getAllMotifFiles();
        for (int j = 0; j < fileNames.size(); j++) {
            String fileName = fileNames.get(j);
            long start = System.currentTimeMillis();
            HmapFindMotif(fileNames.get(j));
            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;

            ResultHolder rh = createResultHolder(fileName, timeElapsed);
            rhs.add(rh);
        }
        MotifFinderUtils.analyzeResults(rhs);
        return rhs;
    }

    /**
     * Implements a Hashmap based search algorithm
     * Multiple (SC) Hashmaps are created: One per each sequence
     * Key value pairs represent {motif_candidate, location}
     * Search is done across hashmpas to find common motifs
     */
    public void HmapFindMotif(String filePfx) {
        String fileName = filePfx + Constants.MOTIF_LENGTH_FILE;
        int motifLength = MotifFinderUtils.motifLengthReader(fileName);

        ArrayList<String> sequenceList = new ArrayList<>();
        fileName = filePfx + Constants.SEQUENCES_FILE;
        sequenceList = MotifFinderUtils.sequenceReader(fileName);

        ArrayList<HashMap<String, Integer>> motifHmaps =
                buildHashMaps(sequenceList, motifLength);

        ArrayList<Integer> motifLocation = new ArrayList<>();
        String possibleMotif = "NOT_FOUND";  // default
        int count = 0;
        for (String possibleM : motifHmaps.get(0).keySet()) {
            count = 0;
            for (int i = 0; i < motifHmaps.size(); i++) {
                if (motifHmaps.get(i).containsKey(possibleM)) {
                    motifLocation.add(motifHmaps.get(i).get(possibleM));
                    count++;
                }
            }
            if (count == motifHmaps.size()) {
                possibleMotif = possibleM;
                break;
            }
        }
        writePredictedResults(filePfx, possibleMotif, motifLocation);
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
    // TODO move this to common utils class
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
