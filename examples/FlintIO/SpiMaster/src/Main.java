
import flint.io.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello form ESP32");

        SpiMaster spiMaster = new SpiMaster("SPI2", 8000000);

        // Set SPI speed
        // spiMaster.setSpeed(8000000);

        // If the SPI pins are not set, the default values ​​will be used (or it may be not set).
        // spiMaster.setMisoPin(12);
        // spiMaster.setMosiPin(13);
        // spiMaster.setClkPin(14);

        spiMaster.open();

        // Write a byte
        spiMaster.write(0x55);

        // Read a byte
        int b = spiMaster.read();

        byte[] tx = new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        byte[] rx = new byte[10];

        // Write a buffer
        spiMaster.write(tx);

        // Read a buffer
        int size = spiMaster.read(rx);

        // Read and write
        size = spiMaster.readWrite(tx, rx, 10);
    }
}
