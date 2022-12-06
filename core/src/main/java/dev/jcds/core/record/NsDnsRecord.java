package dev.jcds.core.record;

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
}
