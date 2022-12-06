package dev.jcds.core.record;

import dev.jcds.core.BytePacketBuffer;

import java.net.Inet6Address;

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
            case 2:
                String ns = buffer.readQName("");
                return new NsDnsRecord(domain, ttl, ns);
            case 5:
                String cname = buffer.readQName("");
                return new CnameDnsRecord(domain, ttl, cname);
            case 15:
                int priority = buffer.readDoubleByte();
                String mx = buffer.readQName("");
                return new MxDnsRecord(domain, ttl, priority, mx);
            case 28:
                String ip6Address = buffer.readIp6Address();
                return new AaaaDnsRecord(domain, ttl, ip6Address);
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
