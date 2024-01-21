/**
 * This class represents a DNA sequence
 * Includes an optional motif at a specified location
 * It also includes meta information such as length
 */

public class Sequence {
    public int length; // Sequence Length
    public String seq; // Actual DNA ATCG char sequence
    public String motif; // Motif ATCG Sequence
    public boolean hasMotif; // Whether the DNA sequence has a motif or not
    public int motifLength; // Motif Length
    public int motifLocation; // Location of the motif within the sequence

    // Constructors
    public Sequence(int length, String seq, String motif, int motifLength, int motifLocation, boolean hasMotif) {
        this.length = length;
        this.seq = seq;
        this.motif = motif;
        this.motifLength = motifLength;
        this.motifLocation = motifLocation;
        this.hasMotif = hasMotif;
    }

    // default basic
    public Sequence() {
        this.length = 0;
        this.seq = "";
        this.motif = "";
        this.motifLength = 0;
        this.motifLocation = 0;
        this.hasMotif = false;
    }

    // Getters and Setters
    public int getLength() {
        return length;
    }

    public String getSeq() {
        return seq;
    }

    public String getMotif() {
        return motif;
    }

    public int getMotifLength() {
        return motifLength;
    }

    public int getMotifLocation() {
        return motifLocation;
    }

    public boolean hasMotif() {
        return hasMotif;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public void setMotifLength(int motifLength) {
        this.motifLength = motifLength;
    }

    public void setMotifLocation(int motifLocation) {
        this.motifLocation = motifLocation;
    }

    public void setHasMotif(boolean hasMotif) {
        this.hasMotif = hasMotif;
    }
}

