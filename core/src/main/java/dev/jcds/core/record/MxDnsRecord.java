package dev.jcds.core.record;

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
}
