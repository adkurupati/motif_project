/*
 * This class is a holder for results.
 * It can be used later for overall performance metrics summarization
 */

public class ResultHolder {
    public int ml; // motif length
    public int sl; // sequence length
    public int sc; // sequence count
    public int index; // dataset index

    public long time; // run time for the search
    public int motifMatch; // found a matching motif
    public int sitesMatch; // sites matching result

    //Constructor
    public ResultHolder(int ml, int sl, int sc) {
        this.ml = ml;
        this.sl = sl;
        this.sc = sc;
    }

    // Setter methods
    public void setMl(int ml) {
        this.ml = ml;
    }

    public void setSl(int sl) {
        this.sl = sl;
    }

    public void setSc(int sc) {
        this.sc = sc;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setMotifMatch(int motifMatch) {
        this.motifMatch = motifMatch;
    }

    public void setSitesMatch(int sitesMatch) {
        this.sitesMatch = sitesMatch;
    }

    public void setTime(long time) {
        this.time = time;
    }

    // Getter methods
    public int getMl() {
        return ml;
    }

    public int getSl() {
        return sl;
    }

    public int getSc() {
        return sc;
    }

    public int getIndex() {
        return index;
    }

    public int getMotifMatch() {
        return motifMatch;
    }

    public int getSitesMatch() {
        return sitesMatch;
    }

    public long getTime() {
        return time;
    }

}
