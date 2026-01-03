
import java.io.*;
import java.net.*;
import flint.net.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String wifiName = "WiFi Name";
        String password = "12345678";

        WiFi.disconnect();
        WiFi.connect(wifiName, password, WiFiAuthMode.WPA_WPA2_PSK);
        System.out.println("Connecting to " + wifiName);

        while(true) {
            Thread.sleep(500);
            if(WiFi.isConnected()) {
                AccessPointRecord ap = WiFi.getAPinfo();
                System.out.println("Connected to " + ap.getSsid());
                System.out.println("RSSI: " + ap.getRssi() + "dBm");
                break;
            }
        }

        Socket socket = new Socket("192.168.1.3", 23);
        socket.setSoTimeout(5000);

        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();
        out.write(new byte[] {72, 101, 108, 108, 111});

        byte[] rxBuff = new byte[1024];
        int rxLen = in.read(rxBuff);
    }
}
