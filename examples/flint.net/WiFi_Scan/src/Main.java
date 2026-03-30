
import flint.net.*;

public class Main {
    public static void main(String[] args) throws Exception {
        while(true) {
            AccessPointRecord[] aps = WiFi.scanNetworks();
            System.out.println("-----------Scan results-----------");
            for(int i = 0; i < aps.length; i++)
                System.out.println(aps[i].getSsid() + " (" + aps[i].getRssi() + "dBm)");
            Thread.sleep(1000);
        }
    }
}
