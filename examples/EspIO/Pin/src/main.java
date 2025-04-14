
import esp.machine.*;

public class main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello form ESP32");
        Pin led = new Pin(15, PinMode.OUTPUT);
        while(true) {
            led.set();
            Thread.sleep(200);
            led.reset();
            Thread.sleep(200);
        }
    }
}
