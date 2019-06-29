import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/* *****************************************************************************
 *  Name: G. Xie
 *  Date: 06/08/2019
 *  Description: MoveToFront.java
 **************************************************************************** */
public class MoveToFront {

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        byte[] dict = new byte[256];
        for (int i = 0; i < 256; i++)
            dict[i] = (byte) i;

        while (!BinaryStdIn.isEmpty()) {
            byte input = BinaryStdIn.readByte();
            // System.out.print(input + "\t");
            byte position = 0;

            for (int i = 0; i < 256; i++) {
                if (dict[i] == input) {
                    position = (byte) i;
                    break;
                }
            }
            BinaryStdOut.write(position);
            for (int i = position & 0xff; i >= 1; i--) {
                dict[i] = dict[i - 1];
            }
            dict[0] = input;
        }
        BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        byte[] dict = new byte[256];
        for (int i = 0; i < 256; i++)
            dict[i] = (byte) i;

        while (!BinaryStdIn.isEmpty()) {
            byte input = BinaryStdIn.readByte();
            int start = input & 0xff;

            BinaryStdOut.write(dict[start]);
            byte temp = dict[start];
            for (int i = start; i >= 1; i--) {
                dict[i] = dict[i - 1];
            }
            dict[0] = temp;
        }
        BinaryStdOut.flush();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-"))
            encode();
        if (args[0].equals("+"))
            decode();
    }
}
