package dev.jcds.core.record;

import java.net.Inet6Address;

public class AaaaDnsRecord extends DnsRecord{
    private Inet6Address address;

    public AaaaDnsRecord(String domain, int ttl, Inet6Address address) {
        super(domain, ttl);
        this.address = address;
    }

    @Override
    public String toString() {
        return "AaaaDnsRecord{" +
                "ipv6Address='" + address.toString() + '\'' +
                ", domain='" + domain + '\'' +
                ", ttl=" + ttl +
                '}';
    }

    public Inet6Address getIpv6Address() {
        return address;
    }
}
