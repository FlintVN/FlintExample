
import machine.gpio.Pin;

public class main {
    public static void main(String[] args) throws Exception {
        Pin.setMode(15, Pin.OUTPUT);
        while(true) {
            Pin.writePin(15, true);
            Thread.sleep(100);
            Pin.writePin(15, false);
            Thread.sleep(100);
        }
    }
}
