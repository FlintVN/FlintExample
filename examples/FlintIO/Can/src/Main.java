import flint.machine.Can;
import flint.machine.CanMessage;
import flint.machine.CanStatus;
import flint.machine.Can.CanMode;  // Thêm import này

import java.util.Arrays;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {
        // CAN0 – main bus
        Can can0 = new Can("CAN0", 0, 500_000)
                .setTxPin(21)
                .setRxPin(22)
                .setMode(CanMode.NORMAL);

        // CAN1 – sniffer / debug
        Can can1 = new Can("CAN1", 1, 250_000)
                .setTxPin(25)
                .setRxPin(26)
                .setMode(CanMode.LISTEN_ONLY);

        can0.open();
        // can1.open(); // Not supported on some platforms

        can0.start();
        // can1.start(); // Not supported on some platforms

        // Send
        can0.writeMessage(
            CanMessage.of(0x123, new byte[]{0x11,0x22}),
            1000
        );

        // Receive on sniffer
        CanMessage rx = can1.receiveMessage(1000);
        if (rx != null) {
            System.out.println("Sniffed: " + rx);
        }

        // Check status
        CanStatus status = can0.getStatus();
        if (status != null) {
            System.out.println("Status: " + status);
        }

        can0.stop();
        // can1.stop(); // Not supported on some platforms

        can0.close();
        // can1.close(); // Not supported on some platforms
    }
}