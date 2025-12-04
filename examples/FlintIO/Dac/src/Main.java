
import flint.machine.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello form ESP32");

        int[] sinValues = new int[50];
        for(int i = 0; i < sinValues.length; i++)
            sinValues[i] = (int)(Math.sin(Math.PI * 2 * i / sinValues.length) * 127) + 128;

        int index = 0;
        Dac dac = new Dac("DAC1");
        while(true) {
            dac.write(sinValues[index]);
            index = (index + 1) % sinValues.length;
            Thread.sleep(1);
        }
    }
}
