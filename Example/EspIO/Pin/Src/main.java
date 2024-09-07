
import machine.gpio.Pin;

public class main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello form ESP32");
        Pin.setMode(15, Pin.OUTPUT);
        long a = 10;
        double d = 31234124.5;
        while(true) {
            Pin.writePin(15, true);
            Thread.sleep(200);
            Pin.writePin(15, false);
            Thread.sleep(200);
        }
    }
}
