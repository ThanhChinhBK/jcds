package dev.jcds.core;

public class QueryType {
    enum Type {
        Unknown(),
        A()
    }


    private final int value;
    Type type;

    private QueryType(int i) {
        this.value = i;
        switch (value) {
            case 1:
                this.type = Type.A;
                break;
            default:
                this.type = Type.Unknown;
                break;
        }
    }

    int toNumber() {
        return value;
    }

    public static QueryType fromValue(int value) {
        return new QueryType(value);
    }

    @Override
    public String toString() {
     switch (type) {
            case A:
                return "A";
            default:
                return "Unknown";
     }

    }

}
