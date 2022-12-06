package dev.jcds.core.record;

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
                "ipv6Address='" + address.toString() + '\'' +
                ", domain='" + domain + '\'' +
                ", ttl=" + ttl +
                '}';
    }

    public String getIp6Address() {
        return ip6Address;
    }
}
