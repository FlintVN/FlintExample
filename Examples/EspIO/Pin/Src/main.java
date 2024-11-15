
import machine.gpio.*;

public class main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello form ESP32");
        Pin.setMode(15, PinMode.OUTPUT);
        while(true) {
            Pin.writePin(15, true);
            Thread.sleep(200);
            Pin.writePin(15, false);
            Thread.sleep(200);
        }
    }
}
