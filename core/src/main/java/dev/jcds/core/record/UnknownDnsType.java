package dev.jcds.core.record;

public class UnknownDnsType extends DnsRecord {
    private int qtype;
    private int dataLength;

    public UnknownDnsType(String domain, int qtype, int dataLength, int ttl) {
        super(domain, ttl);
        this.qtype = qtype;
        this.dataLength = dataLength;
        this.qtypeString = "UNKNOWN";
    }

    public int getQtype() {
        return qtype;
    }

    public int getDataLength() {
        return dataLength;
    }

    @Override
    public String toString() {
        return "UnknownDnsType{" +
                "qtype=" + qtype +
                ", dataLength=" + dataLength +
                ", domain='" + domain + '\'' +
                ", ttl=" + ttl +
                '}';
    }
}