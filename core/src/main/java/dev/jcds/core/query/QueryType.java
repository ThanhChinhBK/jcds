package dev.jcds.core.query;

public class QueryType {

    public final int value;

    protected QueryType(int i) {
        this.value = i;
    }

    int toNumber() {
        return value;
    }

    public static QueryType fromValue(int value) {
        switch (value) {
            case 1:
                return new AQueryType(value);
            default:
                return new QueryType(value);
        }
    }

    @Override
    public String toString() {
        return "Unknown";
    }

}