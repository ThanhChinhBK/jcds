package dev.jcds.core;

import java.util.Vector;

public class DnsPacket {
    public DnsHeader header;
    public Vector<DnsQuestion> questions;
    public Vector<DnsRecord> answers;
    public Vector<DnsRecord> authorities;
    public Vector<DnsRecord> resources;

    public DnsPacket() {
        header = new DnsHeader();
        questions = new Vector<DnsQuestion>();
        answers = new Vector<DnsRecord>();
        authorities = new Vector<DnsRecord>();
        resources = new Vector<DnsRecord>();
    }

    static DnsPacket fromBuffer(BytePacketBuffer buffer) {
        DnsPacket result = new DnsPacket();
        result.header.read(buffer);

        for (int i = 0; i < result.header.questionCount; i++) {
            DnsQuestion question = new DnsQuestion(
                    "",
                    QueryType.fromValue(0) // UNKNOWN
            );
            question.read(buffer);
            result.questions.add(question);
        }

        for (int i = 0; i < result.header.answerCount; i++) {
            DnsRecord record = DnsRecord.read(buffer);
            result.answers.add(record);
        }

        for (int i = 0; i < result.header.authorityEntriesCount; i++) {
            DnsRecord record = DnsRecord.read(buffer);
            result.authorities.add(record);
        }

        for (int i = 0; i < result.header.resourceEntriesCount; i++) {
            DnsRecord record = DnsRecord.read(buffer);
            result.resources.add(record);
        }

        return result;
    }

}
