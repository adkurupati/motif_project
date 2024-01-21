import java.util.*;
import java.io.*;

/*
 * Utility functions for Motif searching
 * All methods are static (Object instantiation not required)
 */

public class MotifFinderUtils {

    // Read list of sequences from sequences.fa file
    public static ArrayList<String> sequenceReader(String file) {
        ArrayList<String> sequenceList = new ArrayList<String>();
        try {
            String fileName = Constants.DATASET_FOLDER + file;
            File f = new File(fileName);
            Scanner scan = new Scanner(f);
            while (scan.hasNext()) {
                String a = scan.nextLine();
                String sequence = scan.nextLine();
                sequenceList.add(sequence);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Unable to read file " + file);
        }
        return sequenceList;
    }

    // Get motif length from relevant motifLength.txt file
    public static int motifLengthReader(String file) {
        int motifLength = 0;
        try {
            String fileName = Constants.DATASET_FOLDER + file;
            File f = new File(fileName);
            Scanner scan = new Scanner(f);
            motifLength = scan.nextInt();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to read file " + file);
        }
        return motifLength;
    }

    // Check if extracted motif matches ground truth
    public static int checkMotifMatch(String filePfx) {
        int motifMatch = 0;

        String fileName = filePfx + Constants.PRED_MOTIF_FILE;
        String predictedMotif = motifReader(fileName);

        fileName = filePfx + Constants.MOTIF_FILE;
        String actualMotif = motifReader(fileName);

        if (actualMotif.equals(predictedMotif)) {
            motifMatch = 1;
        }
        return motifMatch;
    }

    // Check if extracted sites match ground truth
    public static int checkSitesMatch(String filePfx) {
        int sitesMatch = 0;

        String fileName = filePfx + Constants.PRED_SITES_FILE;
        String predictedLocation = sitesReader(fileName);

        fileName = filePfx + Constants.SITES_FILE;
        String actualLocation = sitesReader(fileName);

        if (actualLocation.equals(predictedLocation)) {
            sitesMatch = 1;
        }
        return sitesMatch;
    }

    // Get motif from relevant motif.txt file
    public static String motifReader(String file) {
        String motif = "";
        try {

            String fileName = Constants.DATASET_FOLDER + file;
            File f = new File(fileName);
            Scanner scan = new Scanner(f);
            motif = scan.next();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to read file " + file);
        }
        return motif;
    }

    // Get sites from relevant sites.txt file
    public static String sitesReader(String file) {
        String motifLocation = "";
        try {
            String fileName = Constants.DATASET_FOLDER + file;
            File f = new File(fileName);
            Scanner scan = new Scanner(f);
            while (scan.hasNext()) {
                motifLocation += scan.nextInt();
            }
        } catch (FileNotFoundException e) {
            System.out.println("sites reader Unable to read file " + file);
        }
        return motifLocation;
    }

    // Sample dataset filename: dataset_ml_8_sl_2000_sc_10_index_4_*.txt
    // Function below allows for extracting the relevant ml, sl, sc, index values
    public static int getIntValueFromFilename(String filename, String prefix) {

        int index = filename.indexOf(prefix) + prefix.length();
        String subFilename = filename.substring(index);

        int index1 = subFilename.indexOf("_");
        String stringValue = subFilename.substring(0, index1);
        int intValue = Integer.parseInt(stringValue);

        return intValue;
    }

    // get all motif filenames from dataset folder
    public static ArrayList<String> getAllMotifFiles() {
        File folder = new File(Constants.DATASET_FOLDER);
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> fileNames = new ArrayList<String>();

        for (int i = 0; i < listOfFiles.length; i++) {
            String fileName = listOfFiles[i].getName();

            if (fileName.endsWith("_motif.txt")) {
                fileNames.add(fileName.substring(0, (fileName.lastIndexOf('_')) + 1));
            }
        }
        return fileNames;
    }

    // Analyze results and summarize key stats
    public static void analyzeResultsSet(ArrayList<ResultHolder> rhs, int ml, int sl, int sc) {
        long totalTime = 0;
        int motifMatchCount = 0;
        int sitesMatchCount = 0;

        int count = 0;
        for (int i = 0; i < rhs.size(); i++) {
            ResultHolder rh = rhs.get(i);
            if ((rh.ml == ml) && (rh.sl == sl) && (rh.sc == sc)) {
                totalTime += rh.time;
                motifMatchCount += rh.motifMatch;
                sitesMatchCount += rh.sitesMatch;
                count += 1;
            }
        }

        float avgTime = ((float) totalTime) / count;
        System.out.println("Result for ml: " + ml + ", sl: " + sl + ", sc: " +
                sc + " = " + avgTime + " ms, motifMatchCount = " +
                motifMatchCount + ", sitesMatchCount = " + sitesMatchCount);
    }

    // Analyze overall dataset results
    public static void analyzeResults(ArrayList<ResultHolder> rhs) {
        // TODO: Avoid constants here, get form command line/params or constants file
        System.out.println("Results for ML variation of 6, 7, 8");
        analyzeResultsSet(rhs, 6, 500, 10);
        analyzeResultsSet(rhs, 7, 500, 10);
        analyzeResultsSet(rhs, 8, 500, 10);

        System.out.println("Results for SC variation of 5, 10, 20");
        analyzeResultsSet(rhs, 8, 500, 5);
        analyzeResultsSet(rhs, 8, 500, 10);
        analyzeResultsSet(rhs, 8, 500, 20);

        System.out.println("Results for SL variation of 5, 10, 20");
        analyzeResultsSet(rhs, 8, 500, 10);
        analyzeResultsSet(rhs, 8, 1000, 10);
        analyzeResultsSet(rhs, 8, 2000, 10);

    }

}
