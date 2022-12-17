package dev.jcds.core;

import dev.jcds.core.query.CnameQueryType;
import dev.jcds.core.query.QueryType;
import dev.jcds.core.record.DnsRecord;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.net.*;

public class StubResolverTest {
    @ParameterizedTest
    @CsvSource({"1,A", "2,NS", "5,CNAME", "15,MX", "28,AAAA"})
    public void testStubResolver(int qtypeInt, String qtypeString) throws IOException {
        String qname = "google.com";
        QueryType qtype = QueryType.fromValue(qtypeInt);

        // Google's DNS server
        String serverIp = "8.8.8.8";
        int serverPort = 53;

        // Bind a UDP socket to an arbitrary port
        DatagramSocket socket = new DatagramSocket(null);
        socket.bind(
                new InetSocketAddress("0.0.0.0", 5566)
        );

        // Build our DNS query
        DnsPacket packet = new DnsPacket();
        packet.header.id = 6666;
        packet.header.questionCount = 1;
        packet.header.recursionDesired = true;
        packet.questions.add(new DnsQuestion(qname, qtype));

        // Use our new write method to write the packet to a buffer...
        BytePacketBuffer buffer = new BytePacketBuffer();
        packet.write(buffer);

        // Use the socket to send the query
        BytePacketBuffer requestBuffer = new BytePacketBuffer();
        DatagramPacket requestPacket = new DatagramPacket(
                buffer.getBuf(),
                buffer.getBuf().length,
                InetAddress.getByName(serverIp),
                serverPort
        );
        socket.send(
                requestPacket
        );

        // To prepare for receiving the response, we'll create a new `BytePacketBuffer`,
        // and ask the socket to write the response directly into our buffer.
        BytePacketBuffer responseBuffer = new BytePacketBuffer();
        DatagramPacket responsePacket = new DatagramPacket(
                responseBuffer.getBuf(),
                responseBuffer.getBuf().length
        );
        socket.receive(
                responsePacket
        );

        // unbind the socket
        socket.close();

        // get response buffer
        responseBuffer.setBuf(responsePacket.getData());


        // As per the previous section, `DnsPacket::from_buffer()` is then used to
        // actually parse the packet after which we can print the response.
        DnsPacket response = DnsPacket.fromBuffer(responseBuffer);

        // Test header
        assert response.header.id == 6666;
        assert response.header.recursionDesired;
        assert !response.header.truncatedMessage;
        assert !response.header.authoritativeAnswer;
        assert response.header.opcode == 0;
        assert response.header.response;
        assert response.header.resultCode == ResultCode.NO_ERROR;
        assert !response.header.checkingDisabled;
        assert !response.header.authenticatedData;
        assert !response.header.z;
        assert response.header.recursionAvailable;
        assert response.header.questionCount == 1;
        if (!(qtype instanceof CnameQueryType)) {
            assert response.header.authorityEntriesCount == 0;
        } else {
            assert response.header.authorityEntriesCount == 1;
        }
        assert response.header.resourceEntriesCount == 0;


        // Test questions
        DnsQuestion question = response.questions.get(0);
        assert question.qname.equals(qname);
        assert question.qtype.toString().equals(qtypeString);

        // Test answers
        for (DnsRecord answer : response.answers) {
            System.out.println(answer.qtypeString);
            assert answer.qtypeString.equals(qtypeString);
        }
    }
}
