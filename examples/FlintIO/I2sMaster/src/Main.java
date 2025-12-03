
import flint.machine.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, Exception {
        System.out.println("Hello form ESP32");

        I2sMaster i2sMaster = new I2sMaster("I2S0");

        i2sMaster.setSckPin(16);
        i2sMaster.setWsPin(17);
        i2sMaster.setSdPin(5);

        i2sMaster.setDataMode(I2sDataMode.STEREO);
        i2sMaster.setDirection(I2sDirection.TX);
        i2sMaster.setSampleRate(44100);
        i2sMaster.setDataBits(8);

        i2sMaster.open();

        byte[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        while(true) {
            i2sMaster.write(data);
        }
    }
}
