package dev.jcds.core.record;

import dev.jcds.core.BytePacketBuffer;

public class NsDnsRecord extends DnsRecord {
    private String ns;

    public NsDnsRecord(String domain, int ttl, String ns) {
        super(domain, ttl);
        this.ns = ns;
    }

    @Override
    public String toString() {
        return "NsDnsRecord{" +
                "ns='" + ns + '\'' +
                ", domain='" + domain + '\'' +
                ", ttl=" + ttl +
                '}';
    }

    public String getNs() {
        return ns;
    }


    public void write(BytePacketBuffer buffer) {
        buffer.writeQName(domain);
        buffer.writeDoubleByte(2); // type
        buffer.writeDoubleByte(1);
        buffer.writeFourByte(ttl);

        int pos = buffer.getPos();
        buffer.writeDoubleByte(0); // length

        buffer.writeQName(ns);
        int size = buffer.getPos() - pos - 2;
        buffer.setDoubleByte(pos, size);

    }
}
