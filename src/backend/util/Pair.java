package backend.util;

/**
 * Created by cuong on 10/30/2015.
 */
public class Pair {
    private String k;
    private String v;

    public Pair(String _k, String _v) {
        k = _k;
        v = _v;
    }

    public Pair() {
        this(null, null);
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    @Override
    public String toString() {
        return String.format("Key: %s - Value: %s", k, v);
    }
}
