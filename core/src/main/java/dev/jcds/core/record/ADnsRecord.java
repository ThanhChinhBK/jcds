package dev.jcds.core.record;

public class ADnsRecord extends DnsRecord {
    private String address;

    public ADnsRecord(String domain, String address, int ttl) {
        super(domain, ttl);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "ADnsRecord{" +
                "address='" + address + '\'' +
                ", domain='" + domain + '\'' +
                ", ttl=" + ttl +
                '}';
    }
}
