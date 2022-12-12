package dev.jcds.core.record;

import dev.jcds.core.BytePacketBuffer;

public class CnameDnsRecord extends DnsRecord {
    private String cname;

    public CnameDnsRecord(String domain, int ttl, String cname) {
        super(domain, ttl);
        this.cname = cname;
    }

    @Override
    public String toString() {
        return "CnameDnsRecord{" +
                "cname='" + cname + '\'' +
                ", domain='" + domain + '\'' +
                ", ttl=" + ttl +
                '}';
    }

    public void write(BytePacketBuffer buffer) {
        buffer.writeQName(domain);
        buffer.writeDoubleByte(5); // type
        buffer.writeDoubleByte(1);
        buffer.writeFourByte(ttl);

        int pos = buffer.getPos();
        buffer.writeDoubleByte(0); // length

        buffer.writeQName(cname);
        int size = buffer.getPos() - pos - 2;
        buffer.setDoubleByte(pos, size);
    }
}
