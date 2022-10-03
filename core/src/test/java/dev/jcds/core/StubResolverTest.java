package dev.jcds.core;

import dev.jcds.core.query.AQueryType;
import dev.jcds.core.query.QueryType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.*;

public class StubResolverTest {
    @Test
    public void testStubResolver() throws IOException {
        String qname = "google.com";
        QueryType qtype = QueryType.fromValue(1); // A

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

        // Use the socket to send the query
        BytePacketBuffer requestBuffer = new BytePacketBuffer();
        socket.send(
                new DatagramPacket(
                        requestBuffer.getBuf(),
                        requestBuffer.getBuf().length,
                        InetAddress.getByName(serverIp),
                        serverPort
                )
        );

        // Receive the response
        BytePacketBuffer responseBuffer = new BytePacketBuffer();
        socket.receive(
                new DatagramPacket(
                        responseBuffer.getBuf(),
                        responseBuffer.getBuf().length
                )
        );

        // Parse the response
        DnsPacket response = DnsPacket.fromBuffer(responseBuffer);
        assert response.header.id == 6666;
    }
}
