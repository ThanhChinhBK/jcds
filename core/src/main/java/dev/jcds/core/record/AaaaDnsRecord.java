package dev.jcds.core.record;

import dev.jcds.core.BytePacketBuffer;

import java.net.Inet6Address;

public class AaaaDnsRecord extends DnsRecord{
    private String ip6Address;

    public AaaaDnsRecord(String domain, int ttl, String ip6Address) {
        super(domain, ttl);
        this.ip6Address = ip6Address;
    }

    @Override
    public String toString() {
        return "AaaaDnsRecord{" +
                "ipv6Address='" + ip6Address + '\'' +
                ", domain='" + domain + '\'' +
                ", ttl=" + ttl +
                '}';
    }

    public String getIp6Address() {
        return ip6Address;
    }

    public void write(BytePacketBuffer buffer) {
        buffer.writeQName(domain);
        buffer.writeDoubleByte(28); // type
        buffer.writeDoubleByte(1);
        buffer.writeFourByte(ttl);
        buffer.writeDoubleByte(16); // length
        buffer.writeIpAddress(ip6Address);
    }
}
