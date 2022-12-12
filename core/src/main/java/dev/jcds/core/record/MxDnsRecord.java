package dev.jcds.core.record;

import dev.jcds.core.BytePacketBuffer;

public class MxDnsRecord extends DnsRecord{
    private int priority;
    private String mx;

    public MxDnsRecord(String domain, int ttl, int priority, String mx) {
        super(domain, ttl);
        this.priority = priority;
        this.mx = mx;
    }

    @Override
    public String toString() {
        return "MxDnsRecord{" +
                "priority=" + priority +
                ", mx='" + mx + '\'' +
                ", domain='" + domain + '\'' +
                ", ttl=" + ttl +
                '}';
    }

    public int getPriority() {
        return priority;
    }

    public String getMx() {
        return mx;
    }


    public void write(BytePacketBuffer buffer){
        buffer.writeQName(domain);
        buffer.writeDoubleByte(15); // type
        buffer.writeDoubleByte(1);
        buffer.writeFourByte(ttl);

        int pos = buffer.getPos();
        buffer.writeDoubleByte(0); // length

        buffer.writeDoubleByte(priority);
        buffer.writeQName(mx);
        int size = buffer.getPos() - pos - 2;
        buffer.setDoubleByte(pos, size);

    }
}
