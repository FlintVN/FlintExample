
import flint.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello form ESP32");

        TouchPad pad = initTouchPad(4);
        Pin led = new Pin(2, PinMode.OUTPUT);
        boolean old = false;

        while(true) {
            int val = pad.read();
            boolean touched = pad.isTouched();
            if(touched != old) {
                old = touched;
                if(touched)
                    led.toggle();
            }
            System.out.println(val);
            Thread.sleep(10);
        }
    }

    private static TouchPad initTouchPad(int pin) throws Exception {
        TouchPad pad = new TouchPad(pin);
        Thread.sleep(50);

        int threshold = 0;
        int N = 100;
        for(int i = 0; i < N; i++)
            threshold += pad.read();
        threshold = threshold * 120 / 100 / N;

        pad.setThreshold(threshold);

        return pad;
    }
}
