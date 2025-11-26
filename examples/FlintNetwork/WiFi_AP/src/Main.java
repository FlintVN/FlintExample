
import flint.network.*;

public class Main {
    public static void main(String[] args) throws Exception {
        WiFi.softAP("ESP32-AP mode with java");
        System.out.println("AP mode is enabled");
    }
}
