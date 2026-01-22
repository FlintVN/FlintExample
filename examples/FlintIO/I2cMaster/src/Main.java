
// This example implements using I2C Master to read time from RTC DS1307

import flint.io.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, Exception {
        System.out.println("Hello form ESP32");

        I2cMaster i2cMaster = new I2cMaster("I2C0");
        i2cMaster.setSdaPin(4);
        i2cMaster.setSclPin(16);
        i2cMaster.open();

        // Write time (12:34:56) to DS1307 (0x68)
        i2cMaster.writeMem(0x68, 0, new byte[] {0x56, 0x34, 0x12});

        byte[] time = new byte[3];
        while(true) {
            // Read time from DS1307 (0x68)
            i2cMaster.readMem(0x68, 0, time);
            printTime(time);
            Thread.sleep(1000);
        }
    }

    private static void printTime(byte[] time) {
        System.out.println(bcdToInt(time[2]) + ":" + bcdToInt(time[1]) + ":" + bcdToInt(time[0]));
    }

    private static int bcdToInt(byte bcd) {
        return (bcd >>> 4) * 10 + (bcd & 0x0F);
    }
}
