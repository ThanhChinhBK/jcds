package dev.jcds.core.record;

import dev.jcds.core.BytePacketBuffer;

public class ADnsRecord extends DnsRecord {
    private String address;

    public ADnsRecord(String domain, String address, int ttl) {
        super(domain, ttl);
        this.address = address;
        this.qtypeString = "A";
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "ADnsRecord{" +
                "address='" + address + '\'' +
                ", domain='" + domain + '\'' +
                ", ttl=" + ttl +
                '}';
    }

    @Override
    public void write(BytePacketBuffer buffer) {
        buffer.writeQName(domain);
        buffer.writeDoubleByte(1);
        buffer.writeDoubleByte(1);
        buffer.writeFourByte(ttl);
        buffer.writeDoubleByte(4);
        buffer.writeIpAddress(address);
    }
}
