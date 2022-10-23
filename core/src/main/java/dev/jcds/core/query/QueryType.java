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
            case 2:
                return new NsQueryType(value);
            case 5:
                return new CnameQueryType(value);
            case 15:
                return new MxQueryType(value);
            case 28:
                return new AaaaQueryType(value);
            default:
                return new QueryType(value);
        }
    }

    @Override
    public String toString() {
        return "Unknown";
    }

}