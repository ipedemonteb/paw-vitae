package ar.edu.itba.paw.models;

public class QueryParam {
    private long value;
    public QueryParam(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
