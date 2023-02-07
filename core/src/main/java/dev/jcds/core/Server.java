package dev.jcds.core;

import dev.jcds.core.query.QueryType;
import dev.jcds.core.record.DnsRecord;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class Server {
    private DnsPacket lookup(String qname, QueryType qtype) throws IOException {
        InetSocketAddress server = new InetSocketAddress("8.8.8.8", 53);
        DatagramSocket socket = new DatagramSocket(null);
        DnsPacket packet = new DnsPacket();

        packet.header.id = 6666;
        packet.header.questionCount = 1;
        packet.header.recursionDesired = true;
        packet.questions.add(new DnsQuestion(qname, qtype));

        BytePacketBuffer requestBuffer = new BytePacketBuffer();
        packet.write(requestBuffer);
        socket.send(new DatagramPacket(
                requestBuffer.getBuf(),
                requestBuffer.getBuf().length,
                server
        ));

        BytePacketBuffer responseBuffer = new BytePacketBuffer();
        DatagramPacket responsePacket = new DatagramPacket(
                responseBuffer.getBuf(),
                responseBuffer.getBuf().length
        );
        socket.receive(responsePacket);
        socket.close();
        responseBuffer.setBuf(responsePacket.getData());
        return DnsPacket.fromBuffer(responseBuffer);
    }


}
