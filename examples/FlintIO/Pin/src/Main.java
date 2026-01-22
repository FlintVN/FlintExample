
import flint.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello form ESP32");
        Pin led = new Pin(2, PinMode.OUTPUT);
        while(true) {
            led.set();
            Thread.sleep(200);
            led.reset();
            Thread.sleep(200);
        }
    }
}
