package dev.jcds.core;

import java.nio.charset.StandardCharsets;

public class BytePacketBuffer {
    private byte[] buf;
    private int pos;
    final int maxJump = 5;

    public BytePacketBuffer() {
        buf = new byte[512];
        pos = 0;
    }

    public int getPos() {
        return pos;
    }

    void step(int steps) {
        pos += steps;
    }

    void seek(int pos) {
        this.pos = pos;
    }

    byte read(){
        if (pos >= 512) {
            throw new IndexOutOfBoundsException("Buffer overflow");
        }
        return buf[pos++];
    }

    byte get(int pos) {
        if (pos >= 512) {
            throw new IndexOutOfBoundsException("Buffer overflow");
        }
        return buf[pos];
    }

    byte[] getRange(int start, int len) {
        if (start + len >= 512) {
            throw new IndexOutOfBoundsException("Buffer overflow");
        }

        byte[] result = new byte[len];
        System.arraycopy(buf, start, result, 0, len);
        return result;
    }

    int readDoubleByte() {
        return (read() << 8) | read();
    }

    int readFourByte() {
        return (read() << 24) | (read() << 16) | (read() << 8) | read();
    }

    /// Read a qname
    ///
    /// The tricky part: Reading domain names, taking labels into consideration.
    /// Will take something like [3]www[6]google[3]com[0] and append
    /// www.google.com to outstr.
    String readQName(String outStr) {
        // Since we might encounter jumps, we'll keep track of our position
        // locally as opposed to using the position within the struct. This
        // allows us to move the shared position to a point past our current
        // qname, while keeping track of our progress on the current qname
        // using this variable.
        int pos = this.pos;

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
            if (jumpPerformed > maxJump) {
                throw new RuntimeException(
                        String.format("limit of %d jumps exceeded", maxJump)
                );
            }

            // At this point, we're always at the beginning of a label. Recall
            // that labels start with a length byte.
            int len = get(pos);

            // If len has the two most significant bit are set, it represents a
            // jump to some other offset in the packet:
            if ((len  & 0xC0) == 0xC0) {
                if (!jumped) {
                    // If we haven't jumped yet, we need to update the shared
                    // position to be the position after the jump. This is
                    // because we're going to be jumping around the packet, and
                    // we don't want the shared position to be affected by that.
                    seek(pos + 2);
                }


                // Read another byte, calculate offset and perform the jump by
                // updating our local position variable
                int offset = get(pos + 1);
                pos = (len & 0xC0) << 8 | offset;

                jumped = true;
                jumpPerformed++;
                continue;
            } else {
                // the base case: len is a normal label length
                // we're reading a normal label, push it into outstr
                pos++;
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

            if (!jumped) {
                seek(pos);
            }

        }
        return outStrBuilder.toString();
    }


}
