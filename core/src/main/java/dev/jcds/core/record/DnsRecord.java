package dev.jcds.core.record;

import dev.jcds.core.BytePacketBuffer;

public class DnsRecord {
    protected String domain;
    protected int ttl;

    protected DnsRecord(String domain, int ttl) {
        this.domain = domain;
        this.ttl = ttl;
    }

    public static DnsRecord read(BytePacketBuffer buffer) {
        String domain = buffer.readQName("");
        int qtype = buffer.readDoubleByte();
        buffer.readDoubleByte(); // class
        int ttl = buffer.readFourByte();
        int dataLength = buffer.readDoubleByte();

        switch (qtype) {
            case 1:
                String address = buffer.readIpAddress();
                return new ADnsRecord(domain, address, ttl);
            default:
                return new UnknownDnsType(domain, qtype, dataLength, ttl);
        }

    }


    public void write(BytePacketBuffer buffer) {
        System.out.println("Skipping record: " + this.toString());
    }

    public String getDomain() {
        return domain;
    }

    public int getTtl() {
        return ttl;
    }

    @Override
    public String toString() {
        return "DnsRecord{" +
                "domain='" + domain + '\'' +
                ", ttl=" + ttl +
                '}';
    }
}
