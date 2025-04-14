
import esp.network.*;

public class main {
    public static void main(String[] args) throws Exception {
        WiFi.softAP("ESP32-AP mode with java");
        System.out.println("AP mode is enabled");
    }
}
