package dev.jcds.core.query;

public class CnameQueryType extends QueryType {

    protected CnameQueryType(int i) {
        super(i);
    }

    @Override
    public String toString() {
        return "CNAME";
    }

}