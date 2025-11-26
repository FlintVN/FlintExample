
import flint.network.*;

public class Main {
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
                System.out.println("Connected to " + ap.getSsid());
                System.out.println("RSSI: " + ap.getRssi() + "dBm");
                break;
            }
            Thread.sleep(1000);
        }
    }
}
