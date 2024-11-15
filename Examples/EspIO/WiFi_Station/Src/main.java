
import java.math.BigInteger;
import machine.gpio.*;
import network.*;

public class main {
    public static void main(String[] args) throws Exception {
        String wifiName = "WiFi Name";
        String password = "Password";

        WiFi.disconnect();
        WiFi.connect(wifiName, password, WiFiAuthMode.WPA_WPA2_PSK);
        System.out.println("Connecting to " + wifiName);

        while(true) {
            if(WiFi.isConnected()) {
                System.out.println("Connected");
                AccessPointRecord ap = WiFi.getAPinfo();
                System.out.println("Connected to " + ap.ssid);
                System.out.println("RSSI: " + ap.rssi + "dBm");
                break;
            }
            Thread.sleep(1000);
        }
    }
}
