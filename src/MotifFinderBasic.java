import java.util.*;
import java.io.*;

/*
 * Basic Motif Finder
 * Implements a very simple exhaustive brute force search
 * While not computationally efficient, it is useful for sanity checks
 */

public class MotifFinderBasic {
    ConfigReader configReader;

    // Constructor
    public MotifFinderBasic(ConfigReader configReader) {
        this.configReader = configReader;
    }

    // Top Level Find Method that should be invoked outside the class
    public ArrayList<ResultHolder> find() {
        ArrayList<ResultHolder> rhs = new ArrayList<>();
        ArrayList<String> fileNames = MotifFinderUtils.getAllMotifFiles();
        for (int j = 0; j < fileNames.size(); j++) {
            String fileName = fileNames.get(j);
            long start = System.currentTimeMillis();
            basicFindMotif(fileNames.get(j));
            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;

            ResultHolder rh = createResultHolder(fileName, timeElapsed);
            rhs.add(rh);
        }
        MotifFinderUtils.analyzeResults(rhs);
        return rhs;
    }

    // Store Results in a ResultsHolder Object for future Stats calculations
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

    /**
     * Implements Simple Brute Force Search
     * Loop through all sequences
     * Take each substring from Sequence0,
     * Compare with substrings exhaustively in other sequences
     */
    public void basicFindMotif(String filePfx) {
        // Get the motif length
        String fileName = filePfx + Constants.MOTIF_LENGTH_FILE;
        int motifLength = MotifFinderUtils.motifLengthReader(fileName);

        // Get Sequences
        ArrayList<String> sequenceList = new ArrayList<>();
        fileName = filePfx + Constants.SEQUENCES_FILE;
        sequenceList = MotifFinderUtils.sequenceReader(fileName);

        // Core Search Lop
        String sequenceCompared = sequenceList.get(0);
        int length = sequenceCompared.length();
        ArrayList<Integer> motifLocation = new ArrayList<>();
        String possibleMotif = "";
        for (int i = 0; i < length - motifLength; i++) {
            possibleMotif = sequenceCompared.substring(i, i + motifLength);
            for (int j = 0; j < sequenceList.size(); j++) {
                String checkSequence = sequenceList.get(j);
                if (checkSequence.contains(possibleMotif)) {
                    motifLocation.add(checkSequence.indexOf(possibleMotif));
                } else {
                    motifLocation.clear();
                    break;
                }
            }
            if (motifLocation.size() == sequenceList.size()) {
                break;
            }
        }
        writePredictedResults(filePfx, possibleMotif, motifLocation);

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

}