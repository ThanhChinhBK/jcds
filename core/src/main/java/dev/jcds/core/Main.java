package dev.jcds.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        byte[] buf = Files.readAllBytes(Path.of("core/src/main/resources/response_packet.txt"));
        BytePacketBuffer buffer = new BytePacketBuffer();
        buffer.setBuf(buf);
        DnsPacket packet = DnsPacket.fromBuffer(buffer);
        System.out.println("Packet header: " + packet.header.toString());
        System.out.println("Packet questions: ");
        for (DnsQuestion question : packet.questions) {
            System.out.println(question.toString());
        }
        System.out.println("Packet answers: ");
        for (DnsRecord record : packet.answers) {
            System.out.println(record.toString());
        }
    }

}
