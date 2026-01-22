
// This example implements using OneWire to read temperature from DS18B20 sensor

import flint.io.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, Exception {
        System.out.println("Hello form ESP32");

        // BitStream uses software (bit-banging) so the name (hardware name) is not important and can be null
        BitStream bitStream = new BitStream(null, 350, 800, 700, 600);
        bitStream.setOutputPin(4);
        bitStream.open();

        byte[] colors = {
            (byte)0x00, (byte)0xFF, (byte)0x00, // red
            (byte)0xFF, (byte)0xFF, (byte)0x00, // yellow
            (byte)0xFF, (byte)0x00, (byte)0x00, // green
            (byte)0xFF, (byte)0x00, (byte)0xFF, // aqua
            (byte)0x00, (byte)0x00, (byte)0xFF, // blue
            (byte)0x00, (byte)0xFF, (byte)0xFF, // purple
            (byte)0xFF, (byte)0xFF, (byte)0xFF, // white
        };

        while(true) {
            for(int i = 0; i < 7; i++) {
                bitStream.write(colors, i * 3, 3);
                Thread.sleep(1000);
            }
        }
    }
}
