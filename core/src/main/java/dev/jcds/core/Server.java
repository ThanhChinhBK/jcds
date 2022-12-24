package dev.jcds.core;

import dev.jcds.core.query.QueryType;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class Server {
    private DnsPacket lookup(String qname, QueryType qtype) throws IOException {
        String dnsServer = "8.8.8.8";
        int dnsPort = 53;
        DatagramSocket socket = new DatagramSocket(null);
        socket.bind(
                new InetSocketAddress(dnsServer, dnsPort)
        );

        DnsPacket dnsPacket = new DnsPacket();
        dnsPacket.header.id = 6666;
        dnsPacket.header.questionCount = 1;
        dnsPacket.header.recursionDesired = true;
        dnsPacket.questions.add(new DnsQuestion(qname, qtype));
        BytePacketBuffer requestBuffer = new BytePacketBuffer();
        dnsPacket.write(requestBuffer);
        socket.send(requestBuffer.getPacket(dnsServer, dnsPort));

        BytePacketBuffer responseBuffer = new BytePacketBuffer();
        dnsPacket.write(responseBuffer);

        DatagramPacket responsePacket = new DatagramPacket(
                responseBuffer.getBuf(),
                responseBuffer.getBuf().length
        );
        socket.receive(responsePacket);
        responseBuffer.setBuf(responsePacket.getData());
        dnsPacket.write(responseBuffer);
        return dnsPacket;
    }
}
