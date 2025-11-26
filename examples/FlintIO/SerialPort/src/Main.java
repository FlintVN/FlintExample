
import flint.machine.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, Exception {
        System.out.println("Hello form ESP32");

        SerialPort uart = new SerialPort("UART1");
        uart.setTxPin(4);
        uart.setRxPin(16);
        uart.open();

        byte[] data = "Hello".getBytes();
        while(true) {
            uart.write(data);
            Thread.sleep(1000);
        }
    }
}
