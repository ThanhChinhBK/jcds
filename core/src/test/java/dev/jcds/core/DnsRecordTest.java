package dev.jcds.core;

import dev.jcds.core.query.AQueryType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class DnsRecordTest {
    @Test
    public void testDnsRecord() throws IOException {
        byte[] buf = Files.readAllBytes(Path.of("src/test/resources/response_packet.txt"));
        BytePacketBuffer buffer = new BytePacketBuffer();
        buffer.setBuf(buf);
        DnsPacket packet = DnsPacket.fromBuffer(buffer);

        // test header
        assert packet.header.id == 60744;
        assert packet.header.recursionDesired;
        assert !packet.header.truncatedMessage;
        assert !packet.header.authoritativeAnswer;
        assert packet.header.opcode == 0;
        assert packet.header.response;
        assert packet.header.resultCode == ResultCode.NO_ERROR;
        assert !packet.header.checkingDisabled;
        assert !packet.header.authenticatedData;
        assert !packet.header.z;
        assert packet.header.recursionAvailable;

        // test questions
        assert packet.header.questionCount == 1;
        DnsQuestion question = packet.questions.get(0);
        assert question.qname.equals("google.com");
        assert question.qtype.toString().equals("A");

        // test answers
        assert packet.header.answerCount == 1;
        DnsRecord record = packet.answers.get(0);
        assert record.domain.equals("google.com");



    }
}
