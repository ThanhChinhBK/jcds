package dev.jcds.core;

import dev.jcds.core.query.QueryType;

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
        buffer.readDoubleByte(); // class
    }

    void write(BytePacketBuffer buffer) {
        buffer.writeQName(qname);
        buffer.writeDoubleByte(qtype.value);
        buffer.writeDoubleByte(1); // class
    }

    @Override
    public String toString() {
        return "DnsQuestion{" +
                "qname='" + qname + '\'' +
                ", qtype=" + qtype +
                '}';
    }
}
