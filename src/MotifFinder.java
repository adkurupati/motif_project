import java.util.*;
import java.io.*;

/**
 * Top Level for running different Motif Finders
 * Motif finder algorithm options are defined in config.txt
 */

public class MotifFinder {
    public static void main(String[] args) {
        ConfigReader configReader = new ConfigReader("config.txt");
        ArrayList<ResultHolder> rhs;

        String algo_to_run = configReader.getValue("ALGO_TO_RUN");
        switch (algo_to_run) {
            case "BASIC":
                System.out.println("Running Basic");
                MotifFinderBasic mf = new MotifFinderBasic(configReader);
                mf.find();
                break;

            case "HMAP":
                System.out.println("Running HMap");
                MotifFinderHmap mfh = new MotifFinderHmap(configReader);
                mfh.find();
                break;

            case "ADAPTIVE":
                System.out.println("Running Adaptive");
                MotifFinderAdaptive mfa = new MotifFinderAdaptive(configReader);
                mfa.find();
                break;

            default:
                System.out.println("Invalid Option");
        }
    }
}
