
import flint.net.*;
import java.net.*;
import java.lang.reflect.*;
import java.io.*;

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

        DatagramSocket socket = new DatagramSocket(9876);

        DatagramPacket sendPacket = new DatagramPacket(new byte[] {72, 101, 108, 108, 111}, 5, InetAddress.getByName("192.168.1.3"), 9876);
        socket.send(sendPacket);

        byte[] receiveBuffer = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

        socket.receive(receivePacket);
    }
}
