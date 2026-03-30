
// This example implements using OneWire to read temperature from DS18B20 sensor

import flint.io.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, Exception {
        System.out.println("Hello form ESP32");

        // OneWire uses software (bit-banging) so the name (hardware name) is not important and can be null
        OneWire oneWire = new OneWire(null);
        oneWire.setPin(4);
        oneWire.open();

        while(true) {
            try {
                oneWire.reset();
                oneWire.write(0xCC); // SKIP ROM
                oneWire.write(0x44); // CONVERT T

                Thread.sleep(1000);

                oneWire.reset();
                oneWire.write(0xCC); // SKIP ROM
                oneWire.write(0xBE); // READ SCRATCHPAD

                int bytel = oneWire.read();
                int byteh = oneWire.read();

                float tmp = (((byteh & 0x0F) << 4) | (bytel >> 4));
                tmp += (float)(bytel & 0x0F) / 16;

                System.out.println("Temperature: " + tmp);
            }
            catch(IOException ex) {
                System.out.println(ex);
                Thread.sleep(1000);
            }
        }
    }
}
