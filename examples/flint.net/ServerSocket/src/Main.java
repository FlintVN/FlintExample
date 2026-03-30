
import java.io.*;
import java.net.*;
import flint.net.*;

public class Main {
    public static void main(String[] args) throws Exception {
        connectToWiFi("WiFi Name", "12345678");

        ServerSocket serverSocket = new ServerSocket(23);
        serverSocket.setSoTimeout(5000);

        Socket client = serverSocket.accept();

        InputStream in = client.getInputStream();
        byte[] rxBuff = new byte[1024];
        int rxLen = in.read(rxBuff);

        OutputStream out = client.getOutputStream();
        out.write(new byte[] {72, 101, 108, 108, 111});
    }

    private static void connectToWiFi(String ssid, String password) throws Exception {
        WiFi.disconnect();
        WiFi.connect(ssid, password, WiFiAuthMode.WPA_WPA2_PSK);
        System.out.println("Connecting to " + ssid);

        while(true) {
            Thread.sleep(500);
            if(WiFi.isConnected()) {
                AccessPointRecord ap = WiFi.getAPinfo();
                System.out.println("Connected to " + ap.getSsid());
                System.out.println("RSSI: " + ap.getRssi() + "dBm");
                break;
            }
        }
    }
}
