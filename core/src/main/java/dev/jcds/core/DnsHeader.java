package dev.jcds.core;

public class DnsHeader {
    int id;
    boolean recursionDesired;
    boolean truncatedMessage;
    boolean authoritativeAnswer;
    byte opcode;
    boolean response;
    ResultCode resultCode;
    boolean checkingDisabled;
    boolean authenticatedData;
    boolean z;
    boolean recursionAvailable;
    int questionCount;
    int answerCount;
    int authorityEntriesCount;
    int resourceEntriesCount;

    public DnsHeader() {
        id = 0;
        recursionDesired = false;
        truncatedMessage = false;
        authoritativeAnswer = false;
        opcode = 0;
        response = false;
        resultCode = ResultCode.NO_ERROR;
        checkingDisabled = false;
        authenticatedData = false;
        z = false;
        recursionAvailable = false;
        questionCount = 0;
        answerCount = 0;
        authorityEntriesCount = 0;
        resourceEntriesCount = 0;
    }

    public void read(BytePacketBuffer buffer) {
        this.id = buffer.readDoubleByte();

        int flags = buffer.readDoubleByte();

        byte a = (byte) (flags >> 8);
        byte b = (byte) (flags & 0xFF);

        this.recursionDesired = (a & (1 << 0)) > 0;
        this.truncatedMessage = (a & (1 << 1)) > 0;
        this.authoritativeAnswer = (a & (1 << 2)) > 0;
        this.opcode = (byte) ((a >> 3) & 0x0F);
        this.response = (a & (1 << 7)) > 0;

        this.resultCode = ResultCode.fromValue(b & 0x0F);
        this.checkingDisabled = (b & (1 << 4)) > 0;
        this.authenticatedData = (b & (1 << 5)) > 0;
        this.z = (b & (1 << 6)) > 0;
        this.recursionAvailable = (b & (1 << 7)) > 0;

        this.questionCount = buffer.readDoubleByte();
        this.answerCount = buffer.readDoubleByte();
        this.authorityEntriesCount = buffer.readDoubleByte();
        this.resourceEntriesCount = buffer.readDoubleByte();
    }

    void write(BytePacketBuffer buffer) {
        buffer.writeDoubleByte(this.id);
        buffer.write(
                (byte) ((this.recursionDesired ? 1 : 0) |
                        (this.truncatedMessage ? 1 : 0) << 1 |
                        (this.authoritativeAnswer ? 1 : 0) << 2 |
                        (this.opcode  << 3) |
                        (this.response ? 1 : 0) << 7)

        );

        buffer.write(
                (byte) (this.resultCode.value |
                        (this.checkingDisabled ? 1 : 0) << 4 |
                        (this.authenticatedData ? 1 : 0) << 5 |
                        (this.z ? 1 : 0) << 6 |
                        (this.recursionAvailable ? 1 : 0) << 7)
        );

        buffer.writeDoubleByte(this.questionCount);
        buffer.writeDoubleByte(this.answerCount);
        buffer.writeDoubleByte(this.authorityEntriesCount);
        buffer.writeDoubleByte(this.resourceEntriesCount);
    }

    @Override
    public String toString() {
        return "DnsHeader{" +
                "id=" + id +
                ", recursionDesired=" + recursionDesired +
                ", truncatedMessage=" + truncatedMessage +
                ", authoritativeAnswer=" + authoritativeAnswer +
                ", opcode=" + opcode +
                ", response=" + response +
                ", resultCode=" + resultCode +
                ", checkingDisabled=" + checkingDisabled +
                ", authenticatedData=" + authenticatedData +
                ", z=" + z +
                ", recursionAvailable=" + recursionAvailable +
                ", questionCount=" + questionCount +
                ", answerCount=" + answerCount +
                ", authorityEntriesCount=" + authorityEntriesCount +
                ", resourceEntriesCount=" + resourceEntriesCount +
                '}';
    }
}
