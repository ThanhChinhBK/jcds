package dev.jcds.core;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class BytePacketBuffer {
    private ByteBuffer buf;

    private static final int LABEL_COMPRESSION = 0xC0;
    private static final int LABEL_MASK = 0xC0;

    public BytePacketBuffer() {
        buf = ByteBuffer.allocate(512);
    }

    public byte[] getBuf() {
        return buf.array();
    }

    public void setBuf(byte[] buf) {
        byte[] newBuf = new byte[512];
        System.arraycopy(buf, 0, newBuf, 0, buf.length);
        this.buf = ByteBuffer.wrap(newBuf);
    }

    public int getPos() {
        return buf.position();
    }

    private void require(int step) {
        if (buf.remaining() < step) {
            throw new RuntimeException("Buffer overflow");
        }
    }

    void step(int steps) {
        require(steps);
        buf.position(buf.position() + steps
        );
    }

    void seek(int pos) {
        if (pos < 0 || pos > buf.capacity()) {
            throw new IllegalArgumentException("Invalid position");
        }
        buf.position(pos);
    }

    int read(){
        require(1);
        return buf.get() & 0xFF;
    }

    void write(int val) {
        require(1);
        buf.put((byte) val);
    }

    int get(int pos) {
        if (pos < 0 || pos > buf.capacity()) {
            throw new IllegalArgumentException("Invalid position");
        }
        return buf.get(pos) & 0xFF;
    }

    public String readIpAddress() {
        long rawAddress = readFourByte();
        return String.format(
                "%d.%d.%d.%d",
                (rawAddress >> 24) & 0xFF,
                (rawAddress >> 16) & 0xFF,
                (rawAddress >> 8) & 0xFF,
                rawAddress & 0xFF
        );
    }

    public String readIp6Address() {
        long rawAddress1 = readFourByte();
        long rawAddress2 = readFourByte();
        long rawAddress3 = readFourByte();
        long rawAddress4 = readFourByte();
        return String.format(
                "%x:%x:%x:%x:%x:%x:%x:%x",
                (rawAddress1 >> 24) & 0xFF,
                (rawAddress1 >> 16) & 0xFF,
                (rawAddress1 >> 8) & 0xFF,
                rawAddress1 & 0xFF,
                (rawAddress2 >> 24) & 0xFF,
                (rawAddress2 >> 16) & 0xFF,
                (rawAddress2 >> 8) & 0xFF,
                rawAddress2 & 0xFF,
                (rawAddress3 >> 24) & 0xFF,
                (rawAddress3 >> 16) & 0xFF,
                (rawAddress3 >> 8) & 0xFF,
                rawAddress3 & 0xFF,
                (rawAddress4 >> 24) & 0xFF,
                (rawAddress4 >> 16) & 0xFF,
                (rawAddress4 >> 8) & 0xFF,
                rawAddress4 & 0xFF
        );
    }

    public void writeIpAddress(String address) {
        String[] parts = address.split("\\.");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid IP address");
        }
        for (String part : parts) {
            write(Integer.parseInt(part));
        }
    }

    byte[] getRange(int start, int len) {
        if (start < 0 || start + len > buf.capacity()) {
            throw new IllegalArgumentException("Invalid position");
        }
        int curr = buf.position();
        seek(start);
        byte[] range = new byte[len];
        this.buf.get(range, 0, len);
        seek(curr);
        return range;
    }

    public int readDoubleByte() {
        require(2);
        return this.buf.getShort() & 0xFFFF;
    }

    public void writeDoubleByte(int val) {
        require(2);
        this.buf.putShort((short) val);
    }

    public int readFourByte() {
        require(4);
        return this.buf.getInt();
    }

    public void writeFourByte(int val) {
        require(4);
        this.buf.putInt(val);
    }

    /// Read a qname
    ///
    /// The tricky part: Reading domain names, taking labels into consideration.
    /// Will take something like [3]www[6]google[3]com[0] and append
    /// www.google.com to outstr.
    public String readQName(String outStr) {
        // Since we might encounter jumps, we'll keep track of our position
        // locally as opposed to using the position within the struct. This
        // allows us to move the shared position to a point past our current
        // qname, while keeping track of our progress on the current qname
        // using this variable.
        int pos = this.getPos();

        // track whether  we've jumped
        boolean jumped = false;
        int jumpPerformed = 0;

        // Our delimiter which we append for each label. Since we don't want a
        // dot at the beginning of the domain name we'll leave it empty for now
        // and set it to "." at the end of the first iteration.
        String delim = "";
        StringBuilder outStrBuilder = new StringBuilder(outStr);
        while (true) {
            // Dns Packets are untrusted data, so we need to be paranoid. Someone
            // can craft a packet with a cycle in the jump instructions. This guards
            // against such packets.
            int MAX_JUMP = 5;
            if (jumpPerformed > MAX_JUMP) {
                throw new RuntimeException(
                        String.format("limit of %d jumps exceeded", MAX_JUMP)
                );
            }

            // At this point, we're always at the beginning of a label. Recall
            // that labels start with a length byte.
            int len = this.get(pos);

            // If len has the two most significant bit are set, it represents a
            // jump to some other offset in the packet:
            if ((len  & LABEL_MASK) == LABEL_COMPRESSION) {
                if (!jumped) {
                    // If we haven't jumped yet, we need to update the shared
                    // position to be the position after the jump. This is
                    // because we're going to be jumping around the packet, and
                    // we don't want the shared position to be affected by that.
                    seek(pos + 2);
                }


                // Read another byte, calculate offset and perform the jump by
                // updating our local position variable
                int b2 = get(pos + 1);
                pos = (len ^ LABEL_MASK) << 8 | b2;
                jumped = true;
                jumpPerformed++;
            } else {
                // move past the length byte
                pos += 1;

                // If we've reached the end of the qname, we're done.
                if (len == 0) {
                    break;
                }

                // Append the delimiter first
                outStrBuilder.append(delim);

                // Append the label itself
                outStrBuilder.append(new String(getRange(pos, len), StandardCharsets.US_ASCII));

                // Update position and delimiter
                pos += len;


                delim = ".";
            }

        }

        if (!jumped) {
            seek(pos);
        }
        return outStrBuilder.toString();
    }

    public void writeQName(String qname) {
        String[] labels = qname.split("\\.");
        for (String label : labels) {
            if (label.length() > 63) {
                throw new RuntimeException("Single label exceeds 63 characters");
            }
            byte[] labelBytes = label.getBytes(StandardCharsets.US_ASCII);
            write(labelBytes.length);
            for (byte labelByte : labelBytes) {
                write(labelByte);
            }
        }
        write(0);
    }


}
