package dev.jcds.core;

public class DnsRecord {
    enum RecordType {
        Unknown(),
        A()
    }

    RecordType type;
    String domain;
    int qtype;
    int dataLength;
    int ttl;
    String address;

    private DnsRecord(String domain, int qtype, int dataLength, int ttl) {
        // Unknown record type initially
        this.type = RecordType.Unknown;
        this.domain = domain;
        this.ttl = ttl;
        this.qtype = qtype;
        this.dataLength = dataLength;
    }

    private DnsRecord(String domain, String address, int ttl) {
        // A record type initially
        this.type = RecordType.A;
        this.domain = domain;
        this.address = address;
        this.ttl = ttl;
    }

    public static DnsRecord read(BytePacketBuffer buffer) {
        String domain = buffer.readQName("");

        int qtype = buffer.readDoubleByte();
        buffer.readDoubleByte(); // class
        int ttl = buffer.readDoubleByte();
        int dataLength = buffer.readDoubleByte();

        switch (qtype) {
            case 1:
                String address = buffer.readIpAddress();
                return new DnsRecord(domain, address, ttl);

            default:
                return new DnsRecord(domain, qtype, dataLength, ttl);
        }
    }

    @Override
    public String toString() {
        return "DnsRecord{" +
                "type=" + type +
                ", domain='" + domain + '\'' +
                ", qtype=" + qtype +
                ", dataLength=" + dataLength +
                ", ttl=" + ttl +
                ", address='" + address + '\'' +
                '}';
    }


}
