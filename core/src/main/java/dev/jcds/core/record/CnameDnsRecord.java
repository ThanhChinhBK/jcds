package dev.jcds.core.record;

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
}
