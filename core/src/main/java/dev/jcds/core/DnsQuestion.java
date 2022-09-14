package dev.jcds.core;

public class DnsQuestion {
    String qname;
    QueryType qtype;

    public DnsQuestion(String qname, QueryType qtype) {
        this.qname = qname;
        this.qtype = qtype;
    }

    public void read(BytePacketBuffer buffer) {
        qname = buffer.readQName(this.qname);
        qtype = QueryType.fromValue(buffer.readDoubleByte());
    }

    @Override
    public String toString() {
        return "DnsQuestion{" +
                "qname='" + qname + '\'' +
                ", qtype=" + qtype +
                '}';
    }
}
