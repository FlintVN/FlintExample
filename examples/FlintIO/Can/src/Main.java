import flint.machine.Can;
import java.util.Arrays;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Can can = new Can("CAN-SelfTest", 1, 500_000).setTxPin(21).setRxPin(22);
            can.open();
            can.start();

            byte[] payload = {0x11, 0x22, 0x33, 0x44};
            Can.CanMessage txMsg = Can.CanMessage.of(0x123, payload);
            can.writeMessage(txMsg, 1000);

            Can.CanMessage rxMsg = can.receiveMessage(1000);
            if (rxMsg != null && Arrays.equals(txMsg.data, rxMsg.data)) {
                System.out.println("Self-test PASSED");
            } else {
                System.out.println("Self-test FAILED");
            }

            can.stop();
            can.close();
        } catch (IOException e) {
            /* Nothing TODO */
        }
    }
}
