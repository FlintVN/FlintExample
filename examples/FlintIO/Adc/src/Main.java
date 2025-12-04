
import flint.machine.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, Exception {
        System.out.println("Hello form ESP32");

        Adc adc = new Adc("ADC1", 0);

        while(true) {
            int val = adc.read();
            System.out.println("Adc value: " + val);
            Thread.sleep(500);
        }
    }
}
